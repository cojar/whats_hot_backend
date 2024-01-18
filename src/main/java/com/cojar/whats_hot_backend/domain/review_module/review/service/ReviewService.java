package com.cojar.whats_hot_backend.domain.review_module.review.service;

import com.cojar.whats_hot_backend.domain.base_module.file.entity.FileDomain;
import com.cojar.whats_hot_backend.domain.base_module.file.entity._File;
import com.cojar.whats_hot_backend.domain.base_module.file.service.FileService;
import com.cojar.whats_hot_backend.domain.member_module.member.entity.Member;
import com.cojar.whats_hot_backend.domain.member_module.member.service.MemberService;
import com.cojar.whats_hot_backend.domain.review_module.review.controller.ReviewController;
import com.cojar.whats_hot_backend.domain.review_module.review.dto.ReviewGetDto;
import com.cojar.whats_hot_backend.domain.review_module.review.entity.Review;
import com.cojar.whats_hot_backend.domain.review_module.review.entity.ReviewStatus;
import com.cojar.whats_hot_backend.domain.review_module.review.repository.ReviewRepository;
import com.cojar.whats_hot_backend.domain.review_module.review.request.ReviewRequest;
import com.cojar.whats_hot_backend.domain.review_module.review_hashtag.service.ReviewHashtagService;
import com.cojar.whats_hot_backend.domain.review_module.review_image.service.ReviewImageService;
import com.cojar.whats_hot_backend.domain.spot_module.spot.entity.Spot;
import com.cojar.whats_hot_backend.domain.spot_module.spot.repository.SpotRepository;
import com.cojar.whats_hot_backend.domain.spot_module.spot.service.SpotService;
import com.cojar.whats_hot_backend.global.errors.exception.ApiResponseException;
import com.cojar.whats_hot_backend.global.response.DataModel;
import com.cojar.whats_hot_backend.global.response.ResCode;
import com.cojar.whats_hot_backend.global.response.ResData;
import com.cojar.whats_hot_backend.global.util.AppConfig;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.web.multipart.MultipartFile;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final SpotRepository spotRepository;

    private final SpotService spotService;
    private final MemberService memberService;
    private final FileService fileService;
    private final ReviewHashtagService reviewHashtagService;
    private final ReviewImageService reviewImageService;

    private final EntityManager entityManager;

    public long count() {
        return this.reviewRepository.count();
    }

    private Review refresh(Review review) {
        entityManager.flush();
        review = this.getReviewById(review.getId());
        entityManager.refresh(review);
        return review;
    }

    @Transactional
    public Review create(ReviewRequest.CreateReview request, List<MultipartFile> images, Errors errors, User user) {

        // request 에러 검증
        this.createValidate(request, errors);

        // images 에러 검증
        this.fileService.validateAll(images);

        Spot spot = this.spotService.getSpotById(request.getSpotId());
        Member author = this.memberService.getUserByUsername(user.getUsername());

        Review review = Review.builder()
                .visitDate(LocalDateTime.of(request.getYear(), request.getMonth(), request.getDay(), 0, 0, 0))
                .title(request.getTitle())
                .content(request.getContent())
                .score(request.getScore())
                .spot(spot)
                .author(author)
                .status(request.getLock() ? ReviewStatus.PRIVATE : ReviewStatus.PUBLIC)
                .build();

        this.reviewRepository.save(review);

        // hashtags 생성
        this.reviewHashtagService.createAll(request.getHashtags(), review);

        // images 생성
        List<_File> files = this.fileService.createAll(images, FileDomain.REVIEW);
        this.reviewImageService.createAll(files, review);

        this.spotService.updateAverageScore(spot, review, true);
        this.spotService.updateReviewCount(spot, true);

        return this.refresh(review);
    }

    private void createValidate(ReviewRequest.CreateReview request, Errors errors) {

        if (errors.hasErrors()) {
            throw new ApiResponseException(
                    ResData.of(
                            ResCode.F_03_01_01,
                            errors
                    )
            );
        }

        if (!this.spotRepository.existsById(request.getSpotId())) {

            errors.rejectValue("spotId", "not exist", "spot that has id does not exist");

            throw new ApiResponseException(
                    ResData.of(
                            ResCode.F_03_01_02,
                            errors
                    )
            );
        }

        try {

            LocalDateTime.of(request.getYear(), request.getMonth(), request.getDay(), 0, 0, 0);

        } catch (DateTimeException e) {

            errors.reject("invalid date", new Object[]{request.getYear(), request.getMonth(), request.getDay()}, "date has invalid value");

            throw new ApiResponseException(
                    ResData.of(
                            ResCode.F_03_01_03,
                            errors
                    )
            );
        }

        if (request.getScore() % 0.5 != 0 || request.getScore() < 0 || request.getScore() > 5) {

            errors.rejectValue("score", "not allowed", "score must have value from 0.0 to 5.0 with 0.5 interval");

            throw new ApiResponseException(
                    ResData.of(
                            ResCode.F_03_01_04,
                            errors
                    )
            );
        }
    }

    public Page<DataModel> getReviewPages(int page, int size, String sort, Long spotId, boolean image, Member member) {

        this.getReviewRagesValidate(page, size, sort, spotId, image);

        List<Sort.Order> sorts = new ArrayList<>();
        if (sort.equals("old")) {
            sorts.add(Sort.Order.asc("create_date"));
        } else if (sort.equals("new")) {
            sorts.add(Sort.Order.desc("create_date"));
        } else {
            sorts.add(Sort.Order.desc("liked"));
            sorts.add(Sort.Order.asc("create_date"));
        }
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(sorts));

        return this.reviewRepository.findAllBySpotAndImages(spotId, image, pageable)
                .map(review -> {
                    DataModel dataModel = DataModel.of(
                            ReviewGetDto.of(review, member),
                            linkTo(ReviewController.class).slash(review.getId())
                    );
                    return dataModel;
                });
    }

    private void getReviewRagesValidate(int page, int size, String sort, Long spotId, boolean image) {

        Errors errors = AppConfig.getMockErrors("review");

        if (spotId == -1) {

            errors.reject("not null", new Object[]{spotId}, "spot id is must required");

            throw new ApiResponseException(
                    ResData.of(
                            ResCode.F_03_02_01,
                            errors
                    )
            );
        }

        if (!this.spotRepository.existsById(spotId)) {

            errors.reject("not exist", new Object[]{spotId}, "spot that has id does not exist");

            throw new ApiResponseException(
                    ResData.of(
                            ResCode.F_03_02_02,
                            errors
                    )
            );
        }

        Spot spot = this.spotService.getSpotById(spotId);

        if (this.reviewRepository.countBySpot(spot) == 0) {

            errors.reject("not exist", new Object[]{spotId}, "review that has spot does not exist");

            throw new ApiResponseException(
                    ResData.of(
                            ResCode.F_03_02_03,
                            errors
                    )
            );
        }

        if (size != 20 && size != 50 && size != 100) {

            errors.reject("not allowed", new Object[]{size}, "size does not allowed");

            throw new ApiResponseException(
                    ResData.of(
                            ResCode.F_03_02_04,
                            errors
                    )
            );
        }

        if (Math.ceil((double) this.reviewRepository.countBySpotAndImages(spotId, image) / size) < page) {

            errors.reject("not exist", new Object[]{page}, "page does not exist");

            throw new ApiResponseException(
                    ResData.of(
                            ResCode.F_03_02_05,
                            errors
                    )
            );
        }

        if (!sort.equals("new") && !sort.equals("old") && !sort.equals("like")) {

            errors.reject("not allowed", new Object[]{sort}, "sort does not allowed");

            throw new ApiResponseException(
                    ResData.of(
                            ResCode.F_03_02_06,
                            errors
                    )
            );
        }
    }

    public Review getReviewById(Long id) {

        this.getReviewByIdValidate(id);

        return this.reviewRepository.findById(id)
                .orElse(null);
    }

    private void getReviewByIdValidate(Long id) {

        Errors errors = AppConfig.getMockErrors("review");

        if (!this.reviewRepository.existsById(id)) {

            errors.reject("not exist", new Object[]{id}, "review that has id does not exist");

            throw new ApiResponseException(
                    ResData.of(
                            ResCode.F_03_03_01,
                            errors
                    )
            );
        }
    }

    @Transactional
    public Review update(ReviewRequest.UpdateReview request, List<MultipartFile> images, Errors errors, Long id, User user) {

        // request 에러 검증
        this.updateValidate(request, images, errors, id, user);

        // images 에러 검증
        this.fileService.validateAll(images);

        Review review = this.getReviewById(id);

        review = review.toBuilder()
                .title((request.getTitle() != null && !request.getTitle().isBlank()) ? request.getTitle() : review.getTitle())
                .content((request.getContent() != null && !request.getContent().isBlank()) ? request.getContent() : review.getContent())
                .score(request.getScore() != null ? request.getScore() : review.getScore())
                .status(request.getLock() != null ? (request.getLock() ? ReviewStatus.PRIVATE : ReviewStatus.PUBLIC) : review.getStatus())
                .build();

        this.reviewRepository.save(review);

        // hashtags 수정
        this.reviewHashtagService.updateAll(request.getHashtags(), review);

        // images 수정
        List<_File> files = this.fileService.createAll(images, FileDomain.REVIEW);
        this.reviewImageService.updateAll(files, review);

        this.spotService.updateAverageScore(review.getSpot(), review, true);

        return this.refresh(review);
    }

    private void updateValidate(ReviewRequest.UpdateReview request, List<MultipartFile> images, Errors errors, Long id, User user) {

        if (!this.reviewRepository.existsById(id)) {
            errors.reject("not exist", new Object[]{id}, "review that has id does not exist");

            throw new ApiResponseException(
                    ResData.of(
                            ResCode.F_03_04_01,
                            errors
                    )
            );
        }

        if (!this.getReviewById(id).getAuthor().getUsername().equals(user.getUsername())) {
            errors.reject("has no authority", new Object[]{user.getUsername()}, "user has no authority to modify this review");

            throw new ApiResponseException(
                    ResData.of(
                            ResCode.F_03_04_02,
                            errors
                    )
            );
        }

        if (errors.hasErrors()) {
            throw new ApiResponseException(
                    ResData.of(
                            ResCode.F_03_04_03,
                            errors
                    )
            );
        }

        if (request.getScore() != null && (request.getScore() % 0.5 != 0 || request.getScore() < 0 || request.getScore() > 5)) {

            errors.rejectValue("score", "not allowed", "score must have value from 0.0 to 5.0 with 0.5 interval");

            throw new ApiResponseException(
                    ResData.of(
                            ResCode.F_03_04_04,
                            errors
                    )
            );
        }
    }

    @Transactional
    public void delete(Long id, User user) {

        this.deleteValidate(id, user);

        Review review = this.getReviewById(id);

        List<_File> files = review.getImages().stream()
                .map(image -> image.getImage())
                .collect(Collectors.toList());
        this.fileService.deleteFile(files);

        Spot spot = this.spotService.getSpotById(review.getSpot().getId());

        this.spotService.updateAverageScore(spot, review, false);
        this.spotService.updateReviewCount(spot, false);

        this.reviewRepository.delete(review);
    }

    private void deleteValidate(Long id, User user) {

        Errors errors = AppConfig.getMockErrors("review");

        if (!this.reviewRepository.existsById(id)) {
            errors.reject("not exist", new Object[]{id}, "review that has id does not exist");

            throw new ApiResponseException(
                    ResData.of(
                            ResCode.F_03_05_01,
                            errors
                    )
            );
        }

        if (!this.getReviewById(id).getAuthor().getUsername().equals(user.getUsername())) {
            errors.reject("has no authority", new Object[]{user.getUsername()}, "user has no authority to delete this review");

            throw new ApiResponseException(
                    ResData.of(
                            ResCode.F_03_05_02,
                            errors
                    )
            );
        }
    }

    @Transactional
    public Review toggleLike(Long id, Member member) {

        this.toggleLikeValidate(id, member);

        Review review = this.getReviewById(id);

        if (review.getLikedMember().contains(member)) {

            review = review.toBuilder()
                    .liked(review.getLiked() - 1)
                    .build();
            review.getLikedMember().remove(member);

        } else {

            review = review.toBuilder()
                    .liked(review.getLiked() + 1)
                    .build();
            review.getLikedMember().add(member);
        }

        this.reviewRepository.save(review);

        return review;
    }

    private void toggleLikeValidate(Long id, Member member) {

        Errors errors = AppConfig.getMockErrors("review");

        if (!this.reviewRepository.existsById(id)) {

            errors.reject("not exist", new Object[]{id}, "review that has id does not exist");

            throw new ApiResponseException(
                    ResData.of(
                            ResCode.F_03_06_01,
                            errors
                    )
            );
        }

        if (this.getReviewById(id).getAuthor().getUsername().equals(member.getUsername())) {

            errors.reject("not authorized", new Object[]{member.getUsername()}, "author cannot like own review");

            throw new ApiResponseException(
                    ResData.of(
                            ResCode.F_03_06_02,
                            errors
                    )
            );
        }
    }

    public Page<DataModel> getMyReviewPages(int page, int size, String sort, Member author) {

        this.getMyReviewPagesValidate(page, size, sort, author);

        List<Sort.Order> sorts = new ArrayList<>();
        if (sort.equals("liked")) {
            sorts.add(Sort.Order.desc("liked"));
            sorts.add(Sort.Order.asc("createDate"));
        } else if (sort.equals("old")) {
            sorts.add(Sort.Order.asc("createDate"));
        } else {
            sorts.add(Sort.Order.desc("createDate"));
        }
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(sorts));

        return this.reviewRepository.findAllByAuthor(author, pageable)
                .map(review -> {
                    DataModel dataModel = DataModel.of(
                            ReviewGetDto.of(review, author),
                            linkTo(ReviewController.class).slash(review.getId())
                    );
                    return dataModel;
                });
    }

    private void getMyReviewPagesValidate(int page, int size, String sort, Member author) {

        Errors errors = AppConfig.getMockErrors("review");

        if (this.reviewRepository.countByAuthor(author) == 0) {

            errors.reject("not exist", new Object[]{author.getUsername()}, "review that has author does not exist");

            throw new ApiResponseException(
                    ResData.of(
                            ResCode.F_03_07_01,
                            errors
                    )
            );
        }

        if (size != 20 && size != 50 && size != 100) {

            errors.reject("not allowed", new Object[]{size}, "size does not allowed");

            throw new ApiResponseException(
                    ResData.of(
                            ResCode.F_03_07_02,
                            errors
                    )
            );
        }

        if (Math.ceil((double) this.reviewRepository.countByAuthor(author) / size) < page) {

            errors.reject("not exist", new Object[]{page}, "page does not exist");

            throw new ApiResponseException(
                    ResData.of(
                            ResCode.F_03_07_03,
                            errors
                    )
            );
        }

        if (!sort.equals("new") && !sort.equals("old") && !sort.equals("like")) {

            errors.reject("not allowed", new Object[]{sort}, "sort does not allowed");

            throw new ApiResponseException(
                    ResData.of(
                            ResCode.F_03_07_04,
                            errors
                    )
            );
        }
    }
}
