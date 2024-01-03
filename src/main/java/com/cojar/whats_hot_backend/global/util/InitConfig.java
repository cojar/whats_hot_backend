package com.cojar.whats_hot_backend.global.util;

import com.cojar.whats_hot_backend.domain.base_module.file.service.FileService;
import com.cojar.whats_hot_backend.domain.base_module.hashtag.service.HashtagService;
import com.cojar.whats_hot_backend.domain.comment_module.comment.entity.Comment;
import com.cojar.whats_hot_backend.domain.comment_module.comment.service.CommentService;
import com.cojar.whats_hot_backend.domain.member_module.member.entity.Member;
import com.cojar.whats_hot_backend.domain.member_module.member.request.MemberRequest;
import com.cojar.whats_hot_backend.domain.member_module.member.service.MemberService;
import com.cojar.whats_hot_backend.domain.review_module.review.entity.Review;
import com.cojar.whats_hot_backend.domain.review_module.review.request.ReviewRequest;
import com.cojar.whats_hot_backend.domain.review_module.review.service.ReviewService;
import com.cojar.whats_hot_backend.domain.spot_module.category.entity.Category;
import com.cojar.whats_hot_backend.domain.spot_module.category.service.CategoryService;
import com.cojar.whats_hot_backend.domain.spot_module.menu_item.dto.MenuItemDto;
import com.cojar.whats_hot_backend.domain.spot_module.menu_item.service.MenuItemService;
import com.cojar.whats_hot_backend.domain.spot_module.spot.entity.Spot;
import com.cojar.whats_hot_backend.domain.spot_module.spot.request.SpotRequest;
import com.cojar.whats_hot_backend.domain.spot_module.spot.service.SpotService;
import com.cojar.whats_hot_backend.domain.spot_module.spot_hashtag.service.SpotHashtagService;
import com.cojar.whats_hot_backend.domain.spot_module.spot_image.service.SpotImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mock.web.MockMultipartFile;

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
                            .passwordConfirm("1234")
                            .email("admin@test.com")
                            .build(),
                    new MockMultipartFile(
                            "profileImage",
                            "test.png",
                            AppConfig.getMediaType("test.png"),
                            this.resourceLoader.getResource("classpath:/static/image/%s".formatted("test.png")).getInputStream()
                    ),
                    true,
                    AppConfig.getMockErrors()
            );

            Member user1 = this.memberService.signup(
                    MemberRequest.Signup.builder()
                            .username("user1")
                            .password("1234")
                            .passwordConfirm("1234")
                            .email("user1@test.com")
                            .build(),
                    new MockMultipartFile(
                            "profileImage",
                            "test.png",
                            AppConfig.getMediaType("test.png"),
                            this.resourceLoader.getResource("classpath:/static/image/%s".formatted("test.png")).getInputStream()
                    ),
                    false,
                    AppConfig.getMockErrors()
            );

            Member user2 = this.memberService.signup(
                    MemberRequest.Signup.builder()
                            .username("user2")
                            .password("1234")
                            .passwordConfirm("1234")
                            .email("user2@test.com")
                            .build(),
                    new MockMultipartFile(
                            "profileImage",
                            "test.png",
                            AppConfig.getMediaType("test.png"),
                            this.resourceLoader.getResource("classpath:/static/image/%s".formatted("test.png")).getInputStream()
                    ),
                    false,
                    AppConfig.getMockErrors()
            );

            // category init data

            // 국내맛집 대분류
            Category gourmet = this.categoryService.create("맛집", 1, -1L);

            // 국내맛집 중분류
            Category koreanFood = this.categoryService.create("한식", 2, gourmet.getId());
            Category asianFood = this.categoryService.create("아시아", 2, gourmet.getId());
            Category westernFood = this.categoryService.create("양식", 2, gourmet.getId());
            Category chineseFood = this.categoryService.create("중식", 2, gourmet.getId());
            Category japaneseFood = this.categoryService.create("일식", 2, gourmet.getId());
            Category snackFood = this.categoryService.create("분식", 2, gourmet.getId());
            Category dessert = this.categoryService.create("디저트", 2, gourmet.getId());

            // 국내맛집 소분류
            Category pigFeetAndNapaWrapsWithPork = this.categoryService.create("족발/보쌈", 3, koreanFood.getId());
            Category noodle = this.categoryService.create("면", 3, koreanFood.getId());
            Category grilledMeat = this.categoryService.create("고기/구이", 3, koreanFood.getId());
            Category stewAndSoup = this.categoryService.create("찜/탕/찌개", 3, koreanFood.getId());
            Category lunchBox = this.categoryService.create("도시락", 3, koreanFood.getId());
            Category midnightSnack = this.categoryService.create("야식", 3, koreanFood.getId());
            Category sashimi = this.categoryService.create("회", 3, japaneseFood.getId());
            Category cutletUdon = this.categoryService.create("돈가스/우동", 3, japaneseFood.getId());
            Category pizza = this.categoryService.create("피자", 3, westernFood.getId());
            Category chicken = this.categoryService.create("치킨", 3, westernFood.getId());
            Category italianCuisine = this.categoryService.create("이탈리안", 3, westernFood.getId());
            Category fastFood = this.categoryService.create("패스트푸드", 3, westernFood.getId());
            Category chineseCuisine = this.categoryService.create("중화", 3, chineseFood.getId());
            Category bakery = this.categoryService.create("제과/빵/커피", 3, dessert.getId());
            Category koreanSnack = this.categoryService.create("떡볶이/순대/튀김/어묵", 3, snackFood.getId());
            Category asianCuisine = this.categoryService.create("아시안푸드", 3, asianFood.getId());


            // 국내여행지 대분류
            Category destination = this.categoryService.create("여행지", 1, -1L);

            // 국내여행지 
            Category Palaces = this.categoryService.create("궁궐", 2, destination.getId());
            Category Parks = this.categoryService.create("공원", 2, destination.getId());
            Category Museums = this.categoryService.create("박물관", 2, destination.getId());
            Category Markets = this.categoryService.create("재래시장", 2, destination.getId());
            Category Landmarks = this.categoryService.create("랜드마크", 2, destination.getId());
            Category Mountain = this.categoryService.create("산", 2, destination.getId());
            Category Ocean = this.categoryService.create("바다", 2, destination.getId());
            Category Valley = this.categoryService.create("계곡", 2, destination.getId());
            Category Ruins = this.categoryService.create("유적", 2, destination.getId());
            Category ThemePark = this.categoryService.create("테마파크", 2, destination.getId());
            Category Activity = this.categoryService.create("액티비티", 2, destination.getId());

            // 숙박 대분류
            Category lodging = this.categoryService.create("숙박", 1, -1L);

            // 숙박 중분류
            Category hotelAndResort = this.categoryService.create("호텔/리조트", 2, lodging.getId());
            Category Pension = this.categoryService.create("펜션", 2, lodging.getId());
            Category poolVilla = this.categoryService.create("풀빌라", 2, lodging.getId());
            Category familyStyle = this.categoryService.create("가족형숙소", 2, lodging.getId());
            Category motel = this.categoryService.create("모텔", 2, lodging.getId());
            Category camping = this.categoryService.create("캠핑", 2, lodging.getId());
            Category guesthouse = this.categoryService.create("게스트하우스", 2, lodging.getId());

            // spot init data
            Spot spot1 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(pigFeetAndNapaWrapsWithPork.getId())
                            .name("장소1")
                            .address("대전 서구 대덕대로 179")
                            .contact("010-1234-5678")
                            .hashtags(
                                    List.of(
                                            "해시태그1",
                                            "해시태그2"
                                    )
                            )
                            .menuItems(
                                    List.of(
                                            MenuItemDto.of("메뉴1", "10,000원"),
                                            MenuItemDto.of("메뉴2", "20,000원"),
                                            MenuItemDto.of("메뉴3", "30,000원")
                                    )
                            )
                            .build(),
                    List.of(new MockMultipartFile(
                            "images",
                            "test.png",
                            AppConfig.getMediaType("test.png"),
                            this.resourceLoader.getResource("classpath:/static/image/%s".formatted("test.png")).getInputStream()
                    )),
                    AppConfig.getMockErrors()
            );

            Spot spot2 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(pigFeetAndNapaWrapsWithPork.getId())
                            .name("장소2")
                            .address("대전 서구 대덕대로 179")
                            .contact("010-1234-5678")
                            .hashtags(
                                    List.of(
                                            "해시태그3",
                                            "해시태그4"
                                    )
                            )
                            .menuItems(
                                    List.of(
                                            MenuItemDto.of("메뉴4", "40,000원"),
                                            MenuItemDto.of("메뉴5", "50,000원"),
                                            MenuItemDto.of("메뉴6", "60,000원")
                                    )
                            )
                            .build(),
                    List.of(new MockMultipartFile(
                            "images",
                            "test.png",
                            AppConfig.getMediaType("test.png"),
                            this.resourceLoader.getResource("classpath:/static/image/%s".formatted("test.png")).getInputStream()
                    )),
                    AppConfig.getMockErrors()
            );

            // review init data
            Review review1 = this.reviewService.create(
                    ReviewRequest.CreateReview.builder()
                            .spotId(spot1.getId())
                            .year(2024)
                            .month(1)
                            .day(1)
                            .title("리뷰제목1")
                            .content("리뷰내용1")
                            .score(4.5)
                            .hashtags(
                                    List.of(
                                            "해시태그1",
                                            "해시태그2"
                                    )
                            )
                            .build(),
                    List.of(new MockMultipartFile(
                            "images",
                            "test.png",
                            AppConfig.getMediaType("test.png"),
                            this.resourceLoader.getResource("classpath:/static/image/%s".formatted("test.png")).getInputStream()
                    )),
                    AppConfig.getMockErrors(),
                    AppConfig.toUser(user1)
            );

            Review review2 = this.reviewService.create(
                    ReviewRequest.CreateReview.builder()
                            .spotId(spot1.getId())
                            .year(2024)
                            .month(1)
                            .day(1)
                            .title("리뷰제목2")
                            .content("리뷰내용2")
                            .score(4.0)
                            .hashtags(
                                    List.of(
                                            "해시태그3",
                                            "해시태그4"
                                    )
                            )
                            .build(),
                    List.of(new MockMultipartFile(
                            "images",
                            "test.png",
                            AppConfig.getMediaType("test.png"),
                            this.resourceLoader.getResource("classpath:/static/image/%s".formatted("test.png")).getInputStream()
                    )),
                    AppConfig.getMockErrors(),
                    AppConfig.toUser(user2)
            );

            // comment init data
            Comment comment1 = this.commentService.create(user1, review1, "댓글내용1", null);
            Comment comment2 = this.commentService.create(user1, review1, "댓글내용2", null);

        };
    }
}
