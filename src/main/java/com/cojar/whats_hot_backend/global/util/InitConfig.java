package com.cojar.whats_hot_backend.global.util;

import com.cojar.whats_hot_backend.domain.spot_module.category.entity.Category;
import com.cojar.whats_hot_backend.domain.spot_module.category.service.CategoryService;
import com.cojar.whats_hot_backend.domain.comment_module.comment.entity.Comment;
import com.cojar.whats_hot_backend.domain.comment_module.comment.service.CommentService;
import com.cojar.whats_hot_backend.domain.base_module.file.entity.SaveFile;
import com.cojar.whats_hot_backend.domain.base_module.file.service.FileService;
import com.cojar.whats_hot_backend.domain.base_module.hashtag.entity.Hashtag;
import com.cojar.whats_hot_backend.domain.base_module.hashtag.service.HashtagService;
import com.cojar.whats_hot_backend.domain.member_module.member.entity.Member;
import com.cojar.whats_hot_backend.domain.member_module.member.entity.MemberRole;
import com.cojar.whats_hot_backend.domain.member_module.member.service.MemberService;
import com.cojar.whats_hot_backend.domain.spot_module.menu_item.entity.MenuItem;
import com.cojar.whats_hot_backend.domain.spot_module.menu_item.service.MenuItemService;
import com.cojar.whats_hot_backend.domain.review_module.review.entity.Review;
import com.cojar.whats_hot_backend.domain.review_module.review.entity.ReviewStatus;
import com.cojar.whats_hot_backend.domain.review_module.review.service.ReviewService;
import com.cojar.whats_hot_backend.domain.spot_module.spot.entity.Spot;
import com.cojar.whats_hot_backend.domain.spot_module.spot.service.SpotService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.List;

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

    @Bean
    public ApplicationRunner runner() {
        return args -> {
            Member admin = this.memberService.signup("admin", "1234", "admin@test.com", List.of(MemberRole.ADMIN, MemberRole.USER));
            Member user1 = this.memberService.signup("user1", "1234", "user1@test.com", List.of(MemberRole.USER));

            Category category1 = this.categoryService.create("맛집", 1, -1L);
            Category category2 = this.categoryService.create("2차", 2, category1.getId());
            Category category3 = this.categoryService.create("3차", 3, category2.getId());

            Hashtag hashtag1 = this.hashtagService.create("해시태그1");
            Hashtag hashtag2 = this.hashtagService.create("해시태그2");

            Spot spot1 = this.spotService.create(category3,
                    "대전 서구 대덕대로 179",
                    "010-1234-5678",
                    List.of(hashtag1, hashtag2));

            Spot spot2 = this.spotService.create(category3,
                    "대전 서구 대덕대로 179",
                    "010-1234-5678",
                    List.of(hashtag1, hashtag2));

            Spot spot3 = this.spotService.create(category3,
                    "대전 서구 대덕대로 179",
                    "010-1234-5678",
                    List.of(hashtag1, hashtag2));

            Spot spot4 = this.spotService.create(category3,
                    "대전 서구 대덕대로 179",
                    "010-1234-5678",
                    List.of(hashtag1, hashtag2));

            Spot spot5 = this.spotService.create(category3,
                    "대전 서구 대덕대로 179",
                    "010-1234-5678",
                    List.of(hashtag1, hashtag2));

            Spot spot6 = this.spotService.create(category3,
                    "대전 서구 대덕대로 179",
                    "010-1234-5678",
                    List.of(hashtag1, hashtag2));

            MenuItem menuItem1 = this.menuItemService.create("메뉴1", "10000원", spot1);
            MenuItem menuItem2 = this.menuItemService.create("메뉴2", "20000원", spot1);
            MenuItem menuItem3 = this.menuItemService.create("메뉴3", "30000원", spot1);

            MenuItem menuItem4 = this.menuItemService.create("메뉴1", "10000원", spot2);
            MenuItem menuItem5 = this.menuItemService.create("메뉴2", "20000원", spot2);
            MenuItem menuItem6 = this.menuItemService.create("메뉴3", "30000원", spot2);

            MenuItem menuItem7 = this.menuItemService.create("메뉴1", "10000원", spot3);
            MenuItem menuItem8 = this.menuItemService.create("메뉴2", "20000원", spot3);
            MenuItem menuItem9 = this.menuItemService.create("메뉴3", "30000원", spot3);

            SaveFile image1 = this.fileService.create(spot1);
            SaveFile image2 = this.fileService.create(spot1);
            SaveFile image3 = this.fileService.create(spot1);

            SaveFile image4 = this.fileService.create(spot2);
            SaveFile image5 = this.fileService.create(spot2);
            SaveFile image6 = this.fileService.create(spot2);

            SaveFile image7 = this.fileService.create(spot3);
            SaveFile image8 = this.fileService.create(spot3);
            SaveFile image9 = this.fileService.create(spot3);

            Review review1 = this.reviewService.create(user1, spot1, LocalDateTime.now(), "리뷰제목1", "리뷰내용1", 4.5, ReviewStatus.PUBLIC);
            SaveFile image10 = this.fileService.create(review1);
            Review review2 = this.reviewService.create(user1, spot1, LocalDateTime.now(), "리뷰제목2", "리뷰내용2", 4.5, ReviewStatus.PUBLIC);
            SaveFile image11 = this.fileService.create(review2);

            Comment comment1 = this.commentService.create(user1, review1, "댓글내용1", null);
        };
    }
}
