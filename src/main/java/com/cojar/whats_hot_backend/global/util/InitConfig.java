package com.cojar.whats_hot_backend.global.util;

import com.cojar.whats_hot_backend.domain.base_module.file.entity.FileDomain;
import com.cojar.whats_hot_backend.domain.base_module.file.entity._File;
import com.cojar.whats_hot_backend.domain.base_module.file.service.FileService;
import com.cojar.whats_hot_backend.domain.base_module.hashtag.service.HashtagService;
import com.cojar.whats_hot_backend.domain.comment_module.comment.entity.Comment;
import com.cojar.whats_hot_backend.domain.comment_module.comment.service.CommentService;
import com.cojar.whats_hot_backend.domain.member_module.member.entity.Member;
import com.cojar.whats_hot_backend.domain.member_module.member.entity.MemberRole;
import com.cojar.whats_hot_backend.domain.member_module.member.request.MemberRequest;
import com.cojar.whats_hot_backend.domain.member_module.member.service.MemberService;
import com.cojar.whats_hot_backend.domain.review_module.review.entity.Review;
import com.cojar.whats_hot_backend.domain.review_module.review.entity.ReviewStatus;
import com.cojar.whats_hot_backend.domain.review_module.review.service.ReviewService;
import com.cojar.whats_hot_backend.domain.spot_module.category.entity.Category;
import com.cojar.whats_hot_backend.domain.spot_module.category.service.CategoryService;
import com.cojar.whats_hot_backend.domain.spot_module.menu_item.dto.MenuItemDto;
import com.cojar.whats_hot_backend.domain.spot_module.menu_item.entity.MenuItem;
import com.cojar.whats_hot_backend.domain.spot_module.menu_item.service.MenuItemService;
import com.cojar.whats_hot_backend.domain.spot_module.spot.entity.Spot;
import com.cojar.whats_hot_backend.domain.spot_module.spot.request.SpotRequest;
import com.cojar.whats_hot_backend.domain.spot_module.spot.service.SpotService;
import com.cojar.whats_hot_backend.domain.spot_module.spot_hashtag.entity.SpotHashtag;
import com.cojar.whats_hot_backend.domain.spot_module.spot_hashtag.service.SpotHashtagService;
import com.cojar.whats_hot_backend.domain.spot_module.spot_image.entity.SpotImage;
import com.cojar.whats_hot_backend.domain.spot_module.spot_image.service.SpotImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class InitConfig {

    private final MemberService memberService;
    private final CategoryService categoryService;
    private final HashtagService hashtagService;
    private final MenuItemService menuItemService;
    private final FileService fileService;
    private final SpotService spotService;
    private final ReviewService reviewService;
    private final CommentService commentService;
    private final SpotHashtagService spotHashtagService;
    private final ResourceLoader resourceLoader;
    private final SpotImageService spotImageService;

    @Bean
    public ApplicationRunner runner() {
        return args -> {

            if (!this.memberService.hasNoMember()) {
                log.info("데이터 초기화 미실행");
                return;
            }

            log.info("데이터 초기화 실행");

            // member init data
            Member admin = this.memberService.signup(
                    MemberRequest.Signup.builder()
                            .username("admin")
                            .password("1234")
                            .email("admin@test.com")
                            .build(),
                    List.of(MemberRole.ADMIN, MemberRole.USER));
            this.memberService.save(admin);

            Member user1 = this.memberService.signup(
                    MemberRequest.Signup.builder()
                            .username("user1")
                            .password("1234")
                            .email("user1@test.com")
                            .build(),
                    List.of(MemberRole.USER));
            this.memberService.save(user1);

            Category category1 = this.categoryService.create("맛집", 1, -1L);
            Category category2 = this.categoryService.create("2차", 2, category1.getId());
            Category category3 = this.categoryService.create("3차-1", 3, category2.getId());
            Category category4 = this.categoryService.create("3차-2", 3, category2.getId());

            // spot init data
            Spot spot1 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(category3.getId())
                            .name("장소1")
                            .address("대전 서구 대덕대로 179")
                            .contact("010-1234-5678")
                            .build()
            );
            this.spotService.save(spot1);

            List<SpotHashtag> spotHashtags1 = this.spotHashtagService.createAll(
                    List.of(
                            "해시태그1",
                            "해시태그2"
                    ),
                    spot1);

            List<MenuItem> menuItems1 = this.menuItemService.createAll(
                    List.of(
                            MenuItemDto.of("메뉴1", "10,000원"),
                            MenuItemDto.of("메뉴2", "20,000원"),
                            MenuItemDto.of("메뉴3", "30,000원")
                    ),
                    spot1);

            String fileName1 = "test.png";
            Resource resource = resourceLoader.getResource("classpath:/static/image/%s".formatted(fileName1));
            MultipartFile _file1 = new MockMultipartFile(
                    "images",
                    fileName1,
                    AppConfig.getMediaType(fileName1),
                    resource.getInputStream()
            );
            List<MultipartFile> _files1 = List.of(_file1);
            List<_File> files1 = this.fileService.createAll(_files1, FileDomain.SPOT);
            this.fileService.saveAll(files1);
            List<SpotImage> images1 = this.spotImageService.createAll(files1, spot1);

            Spot spot2 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(category3.getId())
                            .name("장소2")
                            .address("대전 서구 대덕대로 179")
                            .contact("010-1234-5678")
                            .build()
            );
            this.spotService.save(spot2);

            Spot spot3 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(category3.getId())
                            .name("장소3")
                            .address("대전 서구 대덕대로 179")
                            .contact("010-1234-5678")
                            .build()
            );
            this.spotService.save(spot3);

            Spot spot4 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(category3.getId())
                            .name("장소4")
                            .address("대전 서구 대덕대로 179")
                            .contact("010-1234-5678")
                            .build()
            );
            this.spotService.save(spot4);

            Spot spot5 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(category3.getId())
                            .name("장소5")
                            .address("대전 서구 대덕대로 179")
                            .contact("010-1234-5678")
                            .build()
            );
            this.spotService.save(spot5);

            Spot spot6 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(category3.getId())
                            .name("장소6")
                            .address("대전 서구 대덕대로 179")
                            .contact("010-1234-5678")
                            .build()
            );
            this.spotService.save(spot6);

            _File image4 = this.fileService.create(spot2);
            _File image5 = this.fileService.create(spot2);
            _File image6 = this.fileService.create(spot2);

            _File image7 = this.fileService.create(spot3);
            _File image8 = this.fileService.create(spot3);
            _File image9 = this.fileService.create(spot3);

            Review review1 = this.reviewService.create(user1, spot1, LocalDateTime.now(), "리뷰제목1", "리뷰내용1", 4.5, ReviewStatus.PUBLIC);
            _File image10 = this.fileService.create(review1);
            Review review2 = this.reviewService.create(user1, spot1, LocalDateTime.now(), "리뷰제목2", "리뷰내용2", 4.5, ReviewStatus.PUBLIC);
            _File image11 = this.fileService.create(review2);

            Comment comment1 = this.commentService.create(user1, review1, "댓글내용1", null);
            Comment comment2 = this.commentService.create(user1, review1, "댓글내용2", null);

        };
    }
}
