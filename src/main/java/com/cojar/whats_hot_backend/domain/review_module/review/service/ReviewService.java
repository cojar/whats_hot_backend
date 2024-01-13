package com.cojar.whats_hot_backend.domain.review_module.review.service;

import com.cojar.whats_hot_backend.domain.base_module.file.entity.FileDomain;
import com.cojar.whats_hot_backend.domain.base_module.file.entity._File;
import com.cojar.whats_hot_backend.domain.base_module.file.service.FileService;
import com.cojar.whats_hot_backend.domain.member_module.member.entity.Member;
import com.cojar.whats_hot_backend.domain.member_module.member.service.MemberService;
import com.cojar.whats_hot_backend.domain.review_module.review.dto.ReviewCreateDto;
import com.cojar.whats_hot_backend.domain.review_module.review.entity.Review;
import com.cojar.whats_hot_backend.domain.review_module.review.entity.ReviewStatus;
import com.cojar.whats_hot_backend.domain.review_module.review.repository.ReviewRepository;
import com.cojar.whats_hot_backend.domain.review_module.review.request.ReviewRequest;
import com.cojar.whats_hot_backend.domain.review_module.review_hashtag.service.ReviewHashtagService;
import com.cojar.whats_hot_backend.domain.review_module.review_image.service.ReviewImageService;
import com.cojar.whats_hot_backend.domain.spot_module.spot.controller.SpotController;
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
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
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
                .status(request.isLock() ? ReviewStatus.PRIVATE : ReviewStatus.PUBLIC)
                .build();

        this.reviewRepository.save(review);

        // hashtags 생성
        this.reviewHashtagService.createAll(request.getHashtags(), review);

        // images 생성
        List<_File> files = this.fileService.createAll(images, FileDomain.REVIEW);
        this.reviewImageService.createAll(files, review);

        this.spotService.updateReview(spot, review);

        entityManager.flush();
        entityManager.refresh(review);

        return review;
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

    public Page<DataModel> getReviewList(int page, int size) {

        Pageable pageable = PageRequest.of(page - 1, size);

        return this.reviewRepository.findAll(pageable)
                .map(review -> {
                    DataModel dataModel = DataModel.of(
                            ReviewCreateDto.of(review),
                            linkTo(SpotController.class).slash(review.getId())
                    );
                    return dataModel;
                });
    }

    @Transactional
    public void delete(Long id, User user) {

        this.deleteValidate(id, user);

        Review review = this.getReviewById(id);

        List<_File> files = review.getImages().stream()
                .map(image -> image.getImage())
                .collect(Collectors.toList());
        this.fileService.deleteFile(files);

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
}
