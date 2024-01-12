package com.cojar.whats_hot_backend.global.util;

import com.cojar.whats_hot_backend.domain.comment_module.comment.entity.Comment;
import com.cojar.whats_hot_backend.domain.comment_module.comment.request.CommentRequest;
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
import com.cojar.whats_hot_backend.domain.spot_module.spot.entity.Spot;
import com.cojar.whats_hot_backend.domain.spot_module.spot.request.SpotRequest;
import com.cojar.whats_hot_backend.domain.spot_module.spot.service.SpotService;
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
    private final SpotService spotService;
    private final ReviewService reviewService;
    private final CommentService commentService;

    private final ResourceLoader resourceLoader;

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
            Category gourmet = this.categoryService.create("맛집", null, true);

            // 국내맛집 중분류
            Category koreanFood = this.categoryService.create("한식", gourmet.getId(),false);
            Category asianFood = this.categoryService.create("아시아", gourmet.getId(),false);
            Category westernFood = this.categoryService.create("양식", gourmet.getId(),false);
            Category chineseFood = this.categoryService.create("중식", gourmet.getId(),false);
            Category japaneseFood = this.categoryService.create("일식", gourmet.getId(), false);
            Category snackFood = this.categoryService.create("분식",  gourmet.getId(), false);
            Category dessert = this.categoryService.create("디저트", gourmet.getId(), false);

            // 국내맛집 소분류
            Category pigFeetAndNapaWrapsWithPork = this.categoryService.create("족발/보쌈", koreanFood.getId(),false);
            Category noodle = this.categoryService.create("면", koreanFood.getId(),false);
            Category grilledMeat = this.categoryService.create("고기/구이", koreanFood.getId(),false);
            Category stewAndSoup = this.categoryService.create("찜/탕/찌개", koreanFood.getId(), false);
            Category lunchBox = this.categoryService.create("도시락", koreanFood.getId(), false);
            Category midnightSnack = this.categoryService.create("야식", koreanFood.getId(), false);
            Category sashimi = this.categoryService.create("회", japaneseFood.getId(), false);
            Category cutletUdon = this.categoryService.create("돈가스/우동", japaneseFood.getId(), false);
            Category pizza = this.categoryService.create("피자", westernFood.getId(), false);
            Category chicken = this.categoryService.create("치킨", westernFood.getId(), false);
            Category italianCuisine = this.categoryService.create("이탈리안", westernFood.getId(), false);
            Category fastFood = this.categoryService.create("패스트푸드", westernFood.getId(), false);
            Category chineseCuisine = this.categoryService.create("중화", chineseFood.getId(), false);
            Category bakery = this.categoryService.create("제과/빵/커피", dessert.getId(), false);
            Category koreanSnack = this.categoryService.create("떡볶이/순대/튀김/어묵", snackFood.getId(), false);
            Category asianCuisine = this.categoryService.create("아시안푸드", asianFood.getId(), false);


            // 국내여행지 대분류
            Category destination = this.categoryService.create("여행지", null, true);

            // 국내여행지 
            Category Palaces = this.categoryService.create("궁궐", destination.getId(), false);
            Category Parks = this.categoryService.create("공원", destination.getId(), false);
            Category Museums = this.categoryService.create("박물관", destination.getId(), false);
            Category Markets = this.categoryService.create("재래시장", destination.getId(), false);
            Category Landmarks = this.categoryService.create("랜드마크", destination.getId(), false);
            Category Mountain = this.categoryService.create("산", destination.getId(), false);
            Category Ocean = this.categoryService.create("바다", destination.getId(), false);
            Category Valley = this.categoryService.create("계곡", destination.getId(), false);
            Category Ruins = this.categoryService.create("유적", destination.getId(), false);
            Category ThemePark = this.categoryService.create("테마파크", destination.getId(), false);
            Category Activity = this.categoryService.create("액티비티", destination.getId(), false);

            // 숙박 대분류
            Category lodging = this.categoryService.create("숙박", null, true);

            // 숙박 중분류
            Category hotelAndResort = this.categoryService.create("호텔/리조트", lodging.getId(),false);
            Category Pension = this.categoryService.create("펜션", lodging.getId(),false);
            Category poolVilla = this.categoryService.create("풀빌라", lodging.getId(),false);
            Category familyStyle = this.categoryService.create("가족형숙소", lodging.getId(),false);
            Category motel = this.categoryService.create("모텔", lodging.getId(), false);
            Category camping = this.categoryService.create("캠핑", lodging.getId(), false);
            Category guesthouse = this.categoryService.create("게스트하우스", lodging.getId(), false);

            // spot init data
            Spot spot1 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(pigFeetAndNapaWrapsWithPork.getId())
                            .name("아저씨족발")
                            .address("대전 서구 대덕대로 157-1")
                            .contact("042-533-9888")
                            .hashtags(
                                    List.of(
                                            "족발",
                                            "보쌈"
                                    )
                            )
                            .menuItems(
                                    List.of(
                                            MenuItemDto.of("오리지널족발(대)", "38,000원"),
                                            MenuItemDto.of("마늘족발(중)", "35,000원"),
                                            MenuItemDto.of("매운양념족발", "30,000원")
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
                            .name("옥천순대매운족발")
                            .address("대전 대덕구 송촌로 9")
                            .contact("042-634-5882")
                            .hashtags(
                                    List.of(
                                            "순대",
                                            "순댓국"
                                    )
                            )
                            .menuItems(
                                    List.of(
                                            MenuItemDto.of("모듬스페셜 (족발, 매운족발, 순대, 머리고기)", "35,000원"),
                                            MenuItemDto.of("모듬세트 (족발, 순대, 머리고기)", "20,000원"),
                                            MenuItemDto.of("매운족발세트 (매운족발, 순대, 머리고기) 중", "25,000원")
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

            Spot spot3 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(pigFeetAndNapaWrapsWithPork.getId())
                            .name("화곡영양족발")
                            .address("서울 강서구 초록마을로2길 48 1층 화곡영양족발")
                            .contact("0507-1319-7914")
                            .hashtags(
                                    List.of(
                                            "족발",
                                            "보쌈"
                                    )
                            )
                            .menuItems(
                                    List.of(
                                            MenuItemDto.of("족발大", "43,000원"),
                                            MenuItemDto.of("족발中", "38,000원"),
                                            MenuItemDto.of("막국수", "7,000원")
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

            Spot spot4 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(sashimi.getId())
                            .name("동신수산")
                            .address("대전 유성구 노은동로75번길 12")
                            .contact("042-476-9968")
                            .hashtags(
                                    List.of(
                                            "생선회",
                                            "알밥"
                                    )
                            )
                            .menuItems(
                                    List.of(
                                            MenuItemDto.of("정식", "21,000원"),
                                            MenuItemDto.of("초밥", "18,000원"),
                                            MenuItemDto.of("알밥", "11,000원")
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

            Spot spot5 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(sashimi.getId())
                            .name("정치망")
                            .address("대전 서구 도안대로 47")
                            .contact("0507-1430-8188")
                            .hashtags(
                                    List.of(
                                            "생선회",
                                            "초밥"
                                    )
                            )
                            .menuItems(
                                    List.of(
                                            MenuItemDto.of("단품 모듬회(2~3인분)(단품우럭+연어/(대)(3인분))", "67,000원"),
                                            MenuItemDto.of("정치망 명품물회(단품)(명품물회+소면)", "35,500원"),
                                            MenuItemDto.of("단품초밥10ps(연어회초밥10ps)", "16,000원")
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

            Spot spot6 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(sashimi.getId())
                            .name("광안리 진양호횟집")
                            .address("부산 수영구 광안해변로344번길 17-20 풍경타워 4층 광안리 진양호횟집")
                            .contact("0507-1305-8436")
                            .hashtags(
                                    List.of(
                                            "생선회",
                                            "오선뷰"
                                    )
                            )
                            .menuItems(
                                    List.of(
                                            MenuItemDto.of("진양호스페샬1인(회리필2회)", "60,000원"),
                                            MenuItemDto.of("진양호A코스1인(회리필1회)", "50,000원"),
                                            MenuItemDto.of("돌돔코스1인(회리필1회)", "80,000원")
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

            Spot spot7 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(cutletUdon.getId())
                            .name("백소정 대전노은점")
                            .address("대전 유성구 은구비남로33번길 68 103호")
                            .contact("070-8703-1009")
                            .hashtags(
                                    List.of(
                                            "일식당",
                                            "돈까스"
                                    )
                            )
                            .menuItems(
                                    List.of(
                                            MenuItemDto.of("마제소바+돈카츠", "14,900원"),
                                            MenuItemDto.of("에비텐어묵우동+돈카츠", "14,900원"),
                                            MenuItemDto.of("모짜렐라치즈카츠(4pcs)", "12,900원")
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

            Spot spot8 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(cutletUdon.getId())
                            .name("테라스키친")
                            .address("대전 중구 대종로480번길 15 2층")
                            .contact("042-220-4128")
                            .hashtags(
                                    List.of(
                                            "이탈리아음식",
                                            "돈까스"
                                    )
                            )
                            .menuItems(
                                    List.of(
                                            MenuItemDto.of("폭챱폭챱 스테이크", "9,900원"),
                                            MenuItemDto.of("알찬 돈가스", "7,000원"),
                                            MenuItemDto.of("치킨 까스", "7,500원")
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

            Spot spot9 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(cutletUdon.getId())
                            .name("소코아 삼산점")
                            .address("울산 남구 왕생로62번길 16 1층")
                            .contact("0507-1370-9737")
                            .hashtags(
                                    List.of(
                                            "카레",
                                            "돈까스"
                                    )
                            )
                            .menuItems(
                                    List.of(
                                            MenuItemDto.of("소코아 카레", "15,000원"),
                                            MenuItemDto.of("치킨치즈카츠", "14,000원"),
                                            MenuItemDto.of("아보카도 새우냉우동", "14,000원")
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

            Spot spot10 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(pizza.getId())
                            .name("오스하우스 대전중앙로지하상가점")
                            .address("대전 중구 중앙로 145 대전중앙로지하상가 C가11")
                            .contact("042-242-2486")
                            .hashtags(
                                    List.of(
                                            "피자",
                                            "한조각피자"
                                    )
                            )
                            .menuItems(
                                    List.of(
                                            MenuItemDto.of("피자한조각", "1,500원"),
                                            MenuItemDto.of("슈퍼디럭스 불갈비 피자", "11,000원"),
                                            MenuItemDto.of("오레오바닐라", "2,500원")
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

            Spot spot11 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(pizza.getId())
                            .name("홀리데이세븐펍")
                            .address("대전 중구 목척7길 11 1층, 2층, 3층")
                            .contact("070-8834-0777")
                            .hashtags(
                                    List.of(
                                            "피자",
                                            "칵테일"
                                    )
                            )
                            .menuItems(
                                    List.of(
                                            MenuItemDto.of("100장 페퍼로니", "25,000원"),
                                            MenuItemDto.of("트리플 치즈피자", "24,000원"),
                                            MenuItemDto.of("하와이안 피자", "25,000원")
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

            Spot spot12 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(pizza.getId())
                            .name("라라코스트 익산영등점")
                            .address("전북 익산시 무왕로11길 7-4")
                            .contact("0507-1496-3230")
                            .hashtags(
                                    List.of(
                                            "피자",
                                            "패밀리레스토랑"
                                    )
                            )
                            .menuItems(
                                    List.of(
                                            MenuItemDto.of("고르곤졸라 피자", "15,500원"),
                                            MenuItemDto.of("봉골레 파스타", "8,500원"),
                                            MenuItemDto.of("불고기 필라프", "10,500원")
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

            Spot spot13 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(chicken.getId())
                            .name("털보바베큐")
                            .address("대전 서구 갈마중로 7")
                            .contact("0507-1356-2428")
                            .hashtags(
                                    List.of(
                                            "치킨",
                                            "닭강정"
                                    )
                            )
                            .menuItems(
                                    List.of(
                                            MenuItemDto.of("숯불 치킨(소금맛)", "20,000원"),
                                            MenuItemDto.of("숯불치킨(갈비맛)", "21,000원"),
                                            MenuItemDto.of("숯불치킨(매콤맛)", "21,000원")
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

            Spot spot14 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(chicken.getId())
                            .name("포미")
                            .address("대전 서구 갈마중로 34 ,")
                            .contact("042-537-1151")
                            .hashtags(
                                    List.of(
                                            "치킨",
                                            "닭강정"
                                    )
                            )
                            .menuItems(
                                    List.of(
                                            MenuItemDto.of("후라이드치킨", "16,000원"),
                                            MenuItemDto.of("양념치킨", "17,000원"),
                                            MenuItemDto.of("매운양념", "17,000원")
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

            Spot spot15 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(chicken.getId())
                            .name("강릉닭강정")
                            .address("강원 강릉시 초당원길 66 1층")
                            .contact("0507-1424-1087")
                            .hashtags(
                                    List.of(
                                            "치킨",
                                            "닭강정"
                                    )
                            )
                            .menuItems(
                                    List.of(
                                            MenuItemDto.of("누룽지 닭강정(1~2인)_기본/매콤", "14,000원"),
                                            MenuItemDto.of("누룽지 닭강정 (2~3인)_기본/매콤", "24,000원"),
                                            MenuItemDto.of("옥수수토핑(소)", "2,000원")
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

            Spot spot16 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(grilledMeat.getId())
                            .name("고기명작 봉명점")
                            .address("대전 유성구 온천북로33번길 39 1층")
                            .contact("0507-1427-9253")
                            .hashtags(
                                    List.of(
                                            "돼지고기구이",
                                            "소고기"
                                    )
                            )
                            .menuItems(
                                    List.of(
                                            MenuItemDto.of("꽃갈비살", "28,000원"),
                                            MenuItemDto.of("살치살", "26,000원"),
                                            MenuItemDto.of("이베리코플루마", "26,000원")
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

            Spot spot17 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(grilledMeat.getId())
                            .name("오백돈 대전본점")
                            .address("대전 서구 둔산남로105번길 18")
                            .contact("0507-1404-5641")
                            .hashtags(
                                    List.of(
                                            "돼지고기구이",
                                            "돼지목살"
                                    )
                            )
                            .menuItems(
                                    List.of(
                                            MenuItemDto.of("오백돈 500g", "50,000원"),
                                            MenuItemDto.of("제주황제목살", "14,000원"),
                                            MenuItemDto.of("큰뚝배기순두부", "8,000원")
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

            Spot spot18 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(grilledMeat.getId())
                            .name("고깃리88번지 대전둔산점")
                            .address("대전 서구 대덕대로185번길 63 1층")
                            .contact("042-471-8852")
                            .hashtags(
                                    List.of(
                                            "돼지고기구이",
                                            "돼지갈비"
                                    )
                            )
                            .menuItems(
                                    List.of(
                                            MenuItemDto.of("고깃리모둠", "48,000원"),
                                            MenuItemDto.of("수제돼지갈비", "15,000원"),
                                            MenuItemDto.of("목살반근", "24,000원")
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

            Spot spot19 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(chineseCuisine.getId())
                            .name("태화장")
                            .address("대전 동구 중앙로203번길 78")
                            .contact("042-256-2407")
                            .hashtags(
                                    List.of(
                                            "중식당",
                                            "중국집"
                                    )
                            )
                            .menuItems(
                                    List.of(
                                            MenuItemDto.of("짜장면", "6,000원"),
                                            MenuItemDto.of("짬뽕", "8,000원"),
                                            MenuItemDto.of("우동", "9,000원")
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

            Spot spot20 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(chineseCuisine.getId())
                            .name("희락반점")
                            .address("대전 중구 중앙로129번길 21")
                            .contact("042-256-0273")
                            .hashtags(
                                    List.of(
                                            "중식당",
                                            "탕수육맛집"
                                    )
                            )
                            .menuItems(
                                    List.of(
                                            MenuItemDto.of("짜장면", "6,000원"),
                                            MenuItemDto.of("짬뽕", "7,000원"),
                                            MenuItemDto.of("탕수육", "17,000원")
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

            Spot spot21 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(chineseCuisine.getId())
                            .name("태원")
                            .address("대전 서구 문정로 19")
                            .contact("0507-1436-8838")
                            .hashtags(
                                    List.of(
                                            "중식당",
                                            "짬뽕맛집"
                                    )
                            )
                            .menuItems(
                                    List.of(
                                            MenuItemDto.of("등심탕수육", "25,000원"),
                                            MenuItemDto.of("삼선짬뽕", "13,000원"),
                                            MenuItemDto.of("매운짜장", "9,000원")
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

            Spot spot22 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(italianCuisine.getId())
                            .name("세컨디포레스트 대전은행점")
                            .address("대전 중구 중앙로164번길 50 1층 세컨디포레스트 대전점")
                            .contact("0507-1477-4030")
                            .hashtags(
                                    List.of(
                                            "스파게티",
                                            "파스타전문"
                                    )
                            )
                            .menuItems(
                                    List.of(
                                            MenuItemDto.of("오이스터 치킨 쉬림프 리조또", "16,000원"),
                                            MenuItemDto.of("들깨 크림 파스타", "12,000원"),
                                            MenuItemDto.of("스테이크 샐러드 룽기 피자", "22,000원")
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

            Spot spot23 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(italianCuisine.getId())
                            .name("만보우노")
                            .address("대전 서구 만년남로3번길 136")
                            .contact("0507-1346-9957")
                            .hashtags(
                                    List.of(
                                            "스파게티",
                                            "파스타전문"
                                    )
                            )
                            .menuItems(
                                    List.of(
                                            MenuItemDto.of("부채살 스테이크", "41,000원"),
                                            MenuItemDto.of("트러플크림 뇨끼", "19,000원"),
                                            MenuItemDto.of("스파이시 새우 크림 링귀니", "20,000원")
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

            Spot spot24 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(italianCuisine.getId())
                            .name("파운드")
                            .address("대전 동구 수향길 25 파운드")
                            .contact("0507-1393-8381")
                            .hashtags(
                                    List.of(
                                            "이탈리아음식",
                                            "파스타전문"
                                    )
                            )
                            .menuItems(
                                    List.of(
                                            MenuItemDto.of("파운드 스테이크 플레이트", "26,900원"),
                                            MenuItemDto.of("서천 김 페스토 파스타", "15,900원"),
                                            MenuItemDto.of("예산 꽈리고추 닭구이", "16,900원")
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

            Spot spot25 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(stewAndSoup.getId())
                            .name("권인순 갈비김치찌개")
                            .address("대전 유성구 노은서로76번길 75-5")
                            .contact("042-477-8529")
                            .hashtags(
                                    List.of(
                                            "찌개",
                                            "전골"
                                    )
                            )
                            .menuItems(
                                    List.of(
                                            MenuItemDto.of("갈비김치전골 大", "65,000원"),
                                            MenuItemDto.of("갈비김치전골 中", "62,000원"),
                                            MenuItemDto.of("갈비김치찌개", "15,000원")
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

            Spot spot26 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(stewAndSoup.getId())
                            .name("정일품두손두부")
                            .address("대전 서구 둔산대로117번길 34 정일품두손두부")
                            .contact("042-484-8004")
                            .hashtags(
                                    List.of(
                                            "두부요리",
                                            "순두부"
                                    )
                            )
                            .menuItems(
                                    List.of(
                                            MenuItemDto.of("얼큰순두부", "9,000원"),
                                            MenuItemDto.of("순두부청국장", "9,000원"),
                                            MenuItemDto.of("콩국수", "9,500원")
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

            Spot spot27 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(stewAndSoup.getId())
                            .name("유성불백")
                            .address("대전 유성구 죽동로242번길 8-12")
                            .contact("042-824-9862")
                            .hashtags(
                                    List.of(
                                            "찌개",
                                            "전골"
                                    )
                            )
                            .menuItems(
                                    List.of(
                                            MenuItemDto.of("유성불백", "11,000원"),
                                            MenuItemDto.of("계란말이", "8,000원"),
                                            MenuItemDto.of("속초명태회무침", "7,000원")
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

            Spot spot28 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(lunchBox.getId())
                            .name("한솥도시락 대전관평점")
                            .address("대전 유성구 테크노4로 133 V샤르망 108호")
                            .contact("042-932-9329")
                            .hashtags(
                                    List.of(
                                            "도시락",
                                            "컵밥"
                                    )
                            )
                            .menuItems(
                                    List.of(
                                            MenuItemDto.of("매화도시락", "10,500원"),
                                            MenuItemDto.of("동백", "6,200원"),
                                            MenuItemDto.of("왕카레돈까스덮밥", "6,100원")
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

            Spot spot29 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(lunchBox.getId())
                            .name("본도시락 대전둔산점")
                            .address("대전 서구 둔산로137번길 27")
                            .contact("042-489-4282")
                            .hashtags(
                                    List.of(
                                            "도시락",
                                            "컵밥"
                                    )
                            )
                            .menuItems(
                                    List.of(
                                            MenuItemDto.of("마라부대볶음 시티락", "9,900원"),
                                            MenuItemDto.of("바싹불고기 시티락", "8,200원"),
                                            MenuItemDto.of("마라부대찌개", "12,900원")
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

            Spot spot30 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(lunchBox.getId())
                            .name("맘스민")
                            .address("대전 유성구 현충원로489번길 39 1층 맘스민")
                            .contact("1577-8548")
                            .hashtags(
                                    List.of(
                                            "도시락",
                                            "컵밥"
                                    )
                            )
                            .menuItems(
                                    List.of(
                                            MenuItemDto.of("직장인도시락", "8,500원"),
                                            MenuItemDto.of("카레도시락", "10,000원"),
                                            MenuItemDto.of("제육볶음도시락", "11,000원")
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

            Spot spot31 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(bakery.getId())
                            .name("성심당 본점")
                            .address("대전 중구 대종로480번길 15")
                            .contact("1588-8069")
                            .hashtags(
                                    List.of(
                                            "베이커리",
                                            "튀김소보로"
                                    )
                            )
                            .menuItems(
                                    List.of(
                                            MenuItemDto.of("튀김시보로", "1,700원"),
                                            MenuItemDto.of("보문산메아리", "6,000원"),
                                            MenuItemDto.of("판타롱부추빵", "2,000원")
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

            Spot spot32 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(bakery.getId())
                            .name("그린베이커리")
                            .address("대전 유성구 테크노4로 98-8 평원오피스텔 101호 그린베이커리")
                            .contact("0507-1313-3497")
                            .hashtags(
                                    List.of(
                                            "베이커리",
                                            "디저트"
                                    )
                            )
                            .menuItems(
                                    List.of(
                                            MenuItemDto.of("딸기 생크림 케이크 2호", "32,000원"),
                                            MenuItemDto.of("말차크라상", "4,900원"),
                                            MenuItemDto.of("치즈타르트", "3,400원")
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

            Spot spot33 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(bakery.getId())
                            .name("에이트")
                            .address("대전 유성구 한밭대로 458 에이트")
                            .contact("042-716-1195")
                            .hashtags(
                                    List.of(
                                            "카페",
                                            "디저트"
                                    )
                            )
                            .menuItems(
                                    List.of(
                                            MenuItemDto.of("크로넛", "6,000원"),
                                            MenuItemDto.of("아메리카노", "5,500원"),
                                            MenuItemDto.of("소금라떼", "7,000원")
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

            Spot spot34 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(fastFood.getId())
                            .name("써브웨이 대전둔산점")
                            .address("대전 서구 둔산로 18 향촌월드프라자 1층 써브웨이 대전둔산점")
                            .contact("0507-1491-5201")
                            .hashtags(
                                    List.of(
                                            "샌드위치",
                                            "패스트푸드"
                                    )
                            )
                            .menuItems(
                                    List.of(
                                            MenuItemDto.of("에그마요", "5,500원"),
                                            MenuItemDto.of("이탈리안 비엠티", "6,700원"),
                                            MenuItemDto.of("햄", "5,800원")
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

            Spot spot35 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(fastFood.getId())
                            .name("버거킹 대전둔산1점")
                            .address("대전 서구 대덕대로 193")
                            .contact("070-7462-6805")
                            .hashtags(
                                    List.of(
                                            "햄버거",
                                            "패스트푸드"
                                    )
                            )
                            .menuItems(
                                    List.of(
                                            MenuItemDto.of("블랙바비큐와퍼 단품", "10,200원"),
                                            MenuItemDto.of("몬스터와퍼", "10,200원"),
                                            MenuItemDto.of("콰트로치즈와퍼", "8,800원")
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

            Spot spot36 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(fastFood.getId())
                            .name("KFC 대전타임월드")
                            .address("대전 서구 둔산로 18 향촌월드프라자")
                            .contact("042-472-4190")
                            .hashtags(
                                    List.of(
                                            "햄버거",
                                            "치킨"
                                    )
                            )
                            .menuItems(
                                    List.of(
                                            MenuItemDto.of("1994윙4조각", "6,500원"),
                                            MenuItemDto.of("켄터키통다리스파이시", "7,000원"),
                                            MenuItemDto.of("징거더블다운맥스", "7,500원")
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

            Spot spot37 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(koreanSnack.getId())
                            .name("떡반집 본점")
                            .address("대전 서구 둔산로 8")
                            .contact("042-472-2921")
                            .hashtags(
                                    List.of(
                                            "종합분식",
                                            "떡볶이맛집"
                                    )
                            )
                            .menuItems(
                                    List.of(
                                            MenuItemDto.of("떡반", "3,500원"),
                                            MenuItemDto.of("치즈떡반", "4,000원"),
                                            MenuItemDto.of("계란, 피자토스트", "3,800원")
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

            Spot spot38 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(koreanSnack.getId())
                            .name("샘머리김밥")
                            .address("대전 서구 청사로 281")
                            .contact("042-485-6662")
                            .hashtags(
                                    List.of(
                                            "종합분식",
                                            "김밥"
                                    )
                            )
                            .menuItems(
                                    List.of(
                                            MenuItemDto.of("김떡만", "20,500원"),
                                            MenuItemDto.of("김떡순", "20,500원"),
                                            MenuItemDto.of("김떡오", "18,500원")
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

            Spot spot39 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(koreanSnack.getId())
                            .name("만포분식")
                            .address("대전 동구 한남로7번길 89 만포분식")
                            .contact("0507-1352-2069")
                            .hashtags(
                                    List.of(
                                            "종합분식",
                                            "호떡"
                                    )
                            )
                            .menuItems(
                                    List.of(
                                            MenuItemDto.of("만포호떡 1인분(3개)", "4,000원"),
                                            MenuItemDto.of("떡볶이 1인분", "3,000원"),
                                            MenuItemDto.of("순대 1인분", "4,000원")
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

            Spot spot40 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(midnightSnack.getId())
                            .name("닭발에미치다")
                            .address("대전 유성구 진잠로102번길 56 화수분1층 닭발에미치다")
                            .contact("0507-1395-0200")
                            .hashtags(
                                    List.of(
                                            "한식",
                                            "닭발"
                                    )
                            )
                            .menuItems(
                                    List.of(
                                            MenuItemDto.of("직화 통뼈닭발", "11,000원"),
                                            MenuItemDto.of("직화 무뼈닭발", "12,000원"),
                                            MenuItemDto.of("국물닭발(통뼈)", "15,000원")
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

            Spot spot41 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(midnightSnack.getId())
                            .name("안서동야곱집우송대점")
                            .address("대전 동구 동대전로 123-2 201호")
                            .contact("0507-1361-1587")
                            .hashtags(
                                    List.of(
                                            "야식",
                                            "곱창"
                                    )
                            )
                            .menuItems(
                                    List.of(
                                            MenuItemDto.of("야채곱창", "10,900원"),
                                            MenuItemDto.of("알곱창", "12,900원"),
                                            MenuItemDto.of("데리야끼막창", "13,500원")
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

            Spot spot42 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(midnightSnack.getId())
                            .name("홍게파는언니 본점")
                            .address("대전 중구 수침로55번길 45")
                            .contact("042-525-0504")
                            .hashtags(
                                    List.of(
                                            "야식",
                                            "홍게"
                                    )
                            )
                            .menuItems(
                                    List.of(
                                            MenuItemDto.of("홍게 솔로(4마리 이상)", "34,900원"),
                                            MenuItemDto.of("홍게 커플(7마리 이상)", "49,900원"),
                                            MenuItemDto.of("귀여운영덕대게 솔로세트", "55,900원")
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

            Spot spot43 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(asianCuisine.getId())
                            .name("아시안탄방")
                            .address("대전 서구 계룡로553번안길 71 1층 아시안탄방")
                            .contact("0507-1320-4489")
                            .hashtags(
                                    List.of(
                                            "베트남음식",
                                            "쌀국수"
                                    )
                            )
                            .menuItems(
                                    List.of(
                                            MenuItemDto.of("소고기쌀국수", "9,500원"),
                                            MenuItemDto.of("비빔쌀국수", "10,000원"),
                                            MenuItemDto.of("볶음쌀국수", "12,500원")
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

            Spot spot44 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(asianCuisine.getId())
                            .name("마라천하마라탕 은행직영점")
                            .address("대전 중구 중앙로156번길 39")
                            .contact("0507-1396-0567")
                            .hashtags(
                                    List.of(
                                            "마라탕",
                                            "마라샹궈"
                                    )
                            )
                            .menuItems(
                                    List.of(
                                            MenuItemDto.of("취향대로 마라탕(소) 1인", "10,000원"),
                                            MenuItemDto.of("취향대로 마라샹궈(소) 1인", "15,000원"),
                                            MenuItemDto.of("계란볶음밥", "6,000원")
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

            Spot spot45 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(asianCuisine.getId())
                            .name("치앙마이방콕")
                            .address("대전 동구 철갑3길 8")
                            .contact("0507-1377-7894")
                            .hashtags(
                                    List.of(
                                            "태국음식",
                                            "커리"
                                    )
                            )
                            .menuItems(
                                    List.of(
                                            MenuItemDto.of("푸팟퐁커리", "36,000원"),
                                            MenuItemDto.of("쉬림프 팟타이", "18,000원"),
                                            MenuItemDto.of("똠양누들", "18,000원")
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

            Spot spot46 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(noodle.getId())
                            .name("오씨칼국수")
                            .address("대전 동구 옛신탄진로 13")
                            .contact("042-627-9972")
                            .hashtags(
                                    List.of(
                                            "칼국수",
                                            "만두"
                                    )
                            )
                            .menuItems(
                                    List.of(
                                            MenuItemDto.of("손칼국수", "8,000원"),
                                            MenuItemDto.of("물총", "13,000원"),
                                            MenuItemDto.of("해물파전", "11,000원")
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

            Spot spot47 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(noodle.getId())
                            .name("광천식당")
                            .address("대전 중구 대종로505번길 29")
                            .contact("042-226-4751")
                            .hashtags(
                                    List.of(
                                            "칼국수",
                                            "두부두루치기"
                                    )
                            )
                            .menuItems(
                                    List.of(
                                            MenuItemDto.of("수육(소)", "22,000원"),
                                            MenuItemDto.of("두부두루치기", "16,000원"),
                                            MenuItemDto.of("칼국수", "7,000원")
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

            Spot spot48 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(noodle.getId())
                            .name("온천칼국수")
                            .address("대전 유성구 온천북로59번길 2")
                            .contact("042-824-6668")
                            .hashtags(
                                    List.of(
                                            "칼국수",
                                            "쭈꾸미"
                                    )
                            )
                            .menuItems(
                                    List.of(
                                            MenuItemDto.of("물총칼국수", "8,500원"),
                                            MenuItemDto.of("쭈꾸미볶음", "95,000원"),
                                            MenuItemDto.of("수육", "14,000원")
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

            Spot spot49 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(Palaces.getId())
                            .name("경복궁")
                            .address("서울 종로구 사직로 161 경복궁")
                            .contact("02-3700-3900")
                            .hashtags(
                                    List.of(
                                            "궁궐",
                                            "한복체험"
                                    )
                            )
                            .menuItems(
                                    List.of(
                                            MenuItemDto.of("대인", "3,000원"),
                                            MenuItemDto.of("외국인", "3,000원"),
                                            MenuItemDto.of("한복을 착용한 자", "0원")
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

            Spot spot50 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(Palaces.getId())
                            .name("창경궁")
                            .address("서울 종로구 창경궁로 185 창경궁")
                            .contact("")
                            .hashtags(
                                    List.of(
                                            "궁궐",
                                            "데이트코스"
                                    )
                            )
                            .menuItems(
                                    List.of(
                                            MenuItemDto.of("대인", "1,000원"),
                                            MenuItemDto.of("외국인", "1,000원"),
                                            MenuItemDto.of("만65세 이상 어르신, 장애인, 유공자", "0원")
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

            Spot spot51 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(Palaces.getId())
                            .name("덕수궁")
                            .address("서울 중구 세종대로 99 덕수궁")
                            .contact("02-771-9951")
                            .hashtags(
                                    List.of(
                                            "궁궐",
                                            "서울데이트"
                                    )
                            )
                            .menuItems(
                                    List.of(
                                            MenuItemDto.of("대인", "1,000원"),
                                            MenuItemDto.of("외국인", "1,000원"),
                                            MenuItemDto.of("만65세 이상 어르신, 장애인, 유공자", "0원")
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

            Spot spot52 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(Parks.getId())
                            .name("전주덕진공원")
                            .address("전북 전주시 덕진구 권삼득로 390-1 전주덕진공원")
                            .contact("063-281-8661")
                            .hashtags(
                                    List.of(
                                            "근린공원",
                                            "연화정도서관"
                                    )
                            )
                            .menuItems(
                                    List.of(
                                            MenuItemDto.of("입장료", "0원"),
                                            MenuItemDto.of("입장료", "0원"),
                                            MenuItemDto.of("입장료", "0원")
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

            Spot spot53 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(Parks.getId())
                            .name("정읍구절초지방정원")
                            .address("전북 정읍시 산내면 매죽리 산186-5")
                            .contact("063-539-5696")
                            .hashtags(
                                    List.of(
                                            "도시",
                                            "테마공원"
                                    )
                            )
                            .menuItems(
                                    List.of(
                                            MenuItemDto.of("성인", "7,000원"),
                                            MenuItemDto.of("청소년,군인,경로우대자", "5,000원"),
                                            MenuItemDto.of("어린이", "3,000원")
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

            Spot spot54 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(Parks.getId())
                            .name("강천산군립공원")
                            .address("전북 순창군 팔덕면 청계리 산263-1")
                            .contact("063-1234-1234")
                            .hashtags(
                                    List.of(
                                            "군립",
                                            "시립공원"
                                    )
                            )
                            .menuItems(
                                    List.of(
                                            MenuItemDto.of("대인", "3,000원"),
                                            MenuItemDto.of("학생", "2,000원"),
                                            MenuItemDto.of("단체 대인", "2,500원")
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

            Spot spot55 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(Museums.getId())
                            .name("국립어린이박물관")
                            .address("세종 어울누리로 130")
                            .contact("044-251-3000")
                            .hashtags(
                                    List.of(
                                            "박물관",
                                            "어린이박물관"
                                    )
                            )
                            .menuItems(
                                    List.of(
                                            MenuItemDto.of("상설전시", "0원"),
                                            MenuItemDto.of("기획전시(일반)", "2,000원"),
                                            MenuItemDto.of("기획전시(단체)", "1,000원")
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

            Spot spot56 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(Museums.getId())
                            .name("세종시립민속박물관")
                            .address("세종 전의면 금사길 75")
                            .contact("044-300-8831")
                            .hashtags(
                                    List.of(
                                            "박물관",
                                            "민속박물관"
                                    )
                            )
                            .menuItems(
                                    List.of(
                                            MenuItemDto.of("입장료", "0원"),
                                            MenuItemDto.of("입장료", "0원"),
                                            MenuItemDto.of("입장료", "0원")
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

            Spot spot57 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(Museums.getId())
                            .name("충청남도산림박물관")
                            .address("세종 금남면 산림박물관길 110")
                            .contact("041-635-7401")
                            .hashtags(
                                    List.of(
                                            "박물관",
                                            "산림자료전시"
                                    )
                            )
                            .menuItems(
                                    List.of(
                                            MenuItemDto.of("어른", "1,500원"),
                                            MenuItemDto.of("청소년", "1,300원"),
                                            MenuItemDto.of("어린이", "700원")
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

            Spot spot58 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(Markets.getId())
                            .name("부평깡통시장")
                            .address("부산 중구 부평1길 48")
                            .contact("0507-1416-1131")
                            .hashtags(
                                    List.of(
                                            "전통시장",
                                            "야시장"
                                    )
                            )
                            .menuItems(
                                    List.of(
                                            MenuItemDto.of("입장료", "0원"),
                                            MenuItemDto.of("입장료", "0원"),
                                            MenuItemDto.of("입장료", "0원")
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

            Spot spot59 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(Markets.getId())
                            .name("부산자갈치시장")
                            .address("부산 중구 자갈치해안로 52 자갈치시장")
                            .contact("051-245-2594")
                            .hashtags(
                                    List.of(
                                            "수산물시장",
                                            "어패류처리전문"
                                    )
                            )
                            .menuItems(
                                    List.of(
                                            MenuItemDto.of("입장료", "0원"),
                                            MenuItemDto.of("입장료", "0원"),
                                            MenuItemDto.of("입장료", "0원")
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

            Spot spot60 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(Markets.getId())
                            .name("구포시장")
                            .address("부산 북구 구포시장2길 7")
                            .contact("051-333-9033")
                            .hashtags(
                                    List.of(
                                            "전통시장",
                                            "재래시장"
                                    )
                            )
                            .menuItems(
                                    List.of(
                                            MenuItemDto.of("입장료", "0원"),
                                            MenuItemDto.of("입장료", "0원"),
                                            MenuItemDto.of("입장료", "0원")
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

            Spot spot61 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(Landmarks.getId())
                            .name("롯데월드타워 서울스카이")
                            .address("서울 송파구 올림픽로 300 117~123층")
                            .contact("02-1661-2000")
                            .hashtags(
                                    List.of(
                                            "전망대",
                                            "랜드마크"
                                    )
                            )
                            .menuItems(
                                    List.of(
                                            MenuItemDto.of("어른", "29,000원"),
                                            MenuItemDto.of("어린이", "25,000원"),
                                            MenuItemDto.of("FAST PASS", "50,000원")
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

            Spot spot62 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(Landmarks.getId())
                            .name("석굴암")
                            .address("경북 경주시 석굴로 238")
                            .contact("054-746-9933")
                            .hashtags(
                                    List.of(
                                            "세계문화유산",
                                            "사찰"
                                    )
                            )
                            .menuItems(
                                    List.of(
                                            MenuItemDto.of("입장료", "0원"),
                                            MenuItemDto.of("입장료", "0원"),
                                            MenuItemDto.of("입장료", "0원")
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

            Spot spot63 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(Landmarks.getId())
                            .name("안동하회마을")
                            .address("경북 안동시 풍천면 전서로 186")
                            .contact("054-852-3588")
                            .hashtags(
                                    List.of(
                                            "문화",
                                            "유적"
                                    )
                            )
                            .menuItems(
                                    List.of(
                                            MenuItemDto.of("어른", "5,000원"),
                                            MenuItemDto.of("청소년", "2,500원"),
                                            MenuItemDto.of("어린이", "1,500원")
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

            Spot spot64 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(Mountain.getId())
                            .name("한라산 ")
                            .address("제주 서귀포시 토평동 산15-1")
                            .contact("064-713-9950")
                            .hashtags(
                                    List.of(
                                            "산",
                                            "하이킹"
                                    )
                            )
                            .menuItems(
                                    List.of(
                                            MenuItemDto.of("입장료", "0원"),
                                            MenuItemDto.of("샤워장(어른)", "600원"),
                                            MenuItemDto.of("샤워장(청소년)", "400원")
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

            Spot spot65 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(Mountain.getId())
                            .name("설악산 ")
                            .address("강원 인제군 북면 한계리")
                            .contact("033-801-0900")
                            .hashtags(
                                    List.of(
                                            "산",
                                            "국립공원"
                                    )
                            )
                            .menuItems(
                                    List.of(
                                            MenuItemDto.of("입장료", "0원"),
                                            MenuItemDto.of("입장료", "0원"),
                                            MenuItemDto.of("입장료", "0원")
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

            Spot spot66 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(Mountain.getId())
                            .name("지리산 ")
                            .address("경남 함양군 마천면 추성리")
                            .contact("055-970-1000")
                            .hashtags(
                                    List.of(
                                            "산",
                                            "등산"
                                    )
                            )
                            .menuItems(
                                    List.of(
                                            MenuItemDto.of("입장료", "0원"),
                                            MenuItemDto.of("입장료", "0원"),
                                            MenuItemDto.of("입장료", "0원")
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

            Spot spot67 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(Ocean.getId())
                            .name("해운대 해수욕장")
                            .address("부산 해운대구 우동")
                            .contact("051-749-7601")
                            .hashtags(
                                    List.of(
                                            "해수욕장",
                                            "해변"
                                    )
                            )
                            .menuItems(
                                    List.of(
                                            MenuItemDto.of("파라솔", "8,000원"),
                                            MenuItemDto.of("튜브", "8,000원"),
                                            MenuItemDto.of("비치베드", "8,000원")
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

            Spot spot68 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(Ocean.getId())
                            .name("학동흑진주몽돌해수욕장")
                            .address("경남 거제시 동부면 학동리")
                            .contact("052-1234-1234")
                            .hashtags(
                                    List.of(
                                            "해수욕장",
                                            "해변"
                                    )
                            )
                            .menuItems(
                                    List.of(
                                            MenuItemDto.of("입장료", "0원"),
                                            MenuItemDto.of("입장료", "0원"),
                                            MenuItemDto.of("입장료", "0원")
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

            Spot spot69 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(Ocean.getId())
                            .name("관매도해수욕장")
                            .address("전남 진도군 조도면 관매도리")
                            .contact("061-544-0151")
                            .hashtags(
                                    List.of(
                                            "해수욕장",
                                            "산림욕"
                                    )
                            )
                            .menuItems(
                                    List.of(
                                            MenuItemDto.of("입장료", "0원"),
                                            MenuItemDto.of("입장료", "0원"),
                                            MenuItemDto.of("입장료", "0원")
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

            Spot spot70 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(Valley.getId())
                            .name("내원사계곡")
                            .address("경남 양산시 하북면 용연리")
                            .contact("055-380-4826")
                            .hashtags(
                                    List.of(
                                            "계곡",
                                            "산림욕"
                                    )
                            )
                            .menuItems(
                                    List.of(
                                            MenuItemDto.of("입장료", "0원"),
                                            MenuItemDto.of("입장료", "0원"),
                                            MenuItemDto.of("입장료", "0원")
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

            Spot spot71 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(Valley.getId())
                            .name("가평용추계곡")
                            .address("경기 가평군 가평읍 승안리")
                            .contact("031-580-9900")
                            .hashtags(
                                    List.of(
                                            "계곡",
                                            "산림욕"
                                    )
                            )
                            .menuItems(
                                    List.of(
                                            MenuItemDto.of("입장료", "0원"),
                                            MenuItemDto.of("입장료", "0원"),
                                            MenuItemDto.of("입장료", "0원")
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

            Spot spot72 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(Valley.getId())
                            .name("백운계곡")
                            .address("경기 포천시 이동면 도평리")
                            .contact("031-538-3028")
                            .hashtags(
                                    List.of(
                                            "계곡",
                                            "산림욕"
                                    )
                            )
                            .menuItems(
                                    List.of(
                                            MenuItemDto.of("입장료", "0원"),
                                            MenuItemDto.of("입장료", "0원"),
                                            MenuItemDto.of("입장료", "0원")
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

            Spot spot73 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(Ruins.getId())
                            .name("경주 첨성대")
                            .address("경북 경주시 인왕동 839-1")
                            .contact("053-1234-1234")
                            .hashtags(
                                    List.of(
                                            "문화",
                                            "유적"
                                    )
                            )
                            .menuItems(
                                    List.of(
                                            MenuItemDto.of("입장료", "0원"),
                                            MenuItemDto.of("입장료", "0원"),
                                            MenuItemDto.of("입장료", "0원")
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

            Spot spot74 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(Ruins.getId())
                            .name("불국사")
                            .address("경북 경주시 불국로 385 불국사")
                            .contact("054-746-9913")
                            .hashtags(
                                    List.of(
                                            "절",
                                            "사찰"
                                    )
                            )
                            .menuItems(
                                    List.of(
                                            MenuItemDto.of("입장료", "0원"),
                                            MenuItemDto.of("입장료", "0원"),
                                            MenuItemDto.of("입장료", "0원")
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

            Spot spot75 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(Ruins.getId())
                            .name("숭례문")
                            .address("서울 중구 세종대로 40")
                            .contact("02-779-8547")
                            .hashtags(
                                    List.of(
                                            "기념물",
                                            "남대문"
                                    )
                            )
                            .menuItems(
                                    List.of(
                                            MenuItemDto.of("입장료", "0원"),
                                            MenuItemDto.of("입장료", "0원"),
                                            MenuItemDto.of("입장료", "0원")
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

            Spot spot76 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(ThemePark.getId())
                            .name("에버랜드 ")
                            .address("경기 용인시 처인구 포곡읍 에버랜드로 199")
                            .contact("031-320-5000")
                            .hashtags(
                                    List.of(
                                            "테마파크",
                                            "동물원"
                                    )
                            )
                            .menuItems(
                                    List.of(
                                            MenuItemDto.of("C종일권", "34,000원"),
                                            MenuItemDto.of("B종일권", "37,000원"),
                                            MenuItemDto.of("종일이용권", "45,000원")
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

            Spot spot77 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(ThemePark.getId())
                            .name("롯데월드 어드벤처")
                            .address("서울 송파구 올림픽로 240")
                            .contact("1661-2000")
                            .hashtags(
                                    List.of(
                                            "테마파크",
                                            "교복대여"
                                    )
                            )
                            .menuItems(
                                    List.of(
                                            MenuItemDto.of("성인 종일", "62,000원"),
                                            MenuItemDto.of("청소년 종일", "54,000원"),
                                            MenuItemDto.of("어린이 종일", "47,000원")
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

            Spot spot78 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(ThemePark.getId())
                            .name("롯데월드 아쿠아리움")
                            .address("서울 송파구 올림픽로 300 롯데월드몰 B1")
                            .contact("1661-2000")
                            .hashtags(
                                    List.of(
                                            "아쿠아리움",
                                            "수족관"
                                    )
                            )
                            .menuItems(
                                    List.of(
                                            MenuItemDto.of("어른 및 청소년", "35,000원"),
                                            MenuItemDto.of("어린이", "29,000원"),
                                            MenuItemDto.of("36개월 미만", "0원")
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

            Spot spot79 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(Activity.getId())
                            .name("스카이라인루지 통영")
                            .address("경남 통영시 발개로 178")
                            .contact("1522-2468")
                            .hashtags(
                                    List.of(
                                            "루지",
                                            "가족여행"
                                    )
                            )
                            .menuItems(
                                    List.of(
                                            MenuItemDto.of("개인-루지 & 스카이라이드 콤보 2회", "27,000원"),
                                            MenuItemDto.of("개인-루지 & 스카이라이드 콤보 3회", "30,000원"),
                                            MenuItemDto.of("개인-루지 & 스카이라이드 콤보 4회", "33,000원")
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

            Spot spot80 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(Activity.getId())
                            .name("제주빅볼랜드")
                            .address("제주 제주시 구좌읍 송당리 산164-4")
                            .contact("1588-6418")
                            .hashtags(
                                    List.of(
                                            "관람",
                                            "체험"
                                    )
                            )
                            .menuItems(
                                    List.of(
                                            MenuItemDto.of("빅볼 성인", "35,000원"),
                                            MenuItemDto.of("빅볼 소인", "30,000원"),
                                            MenuItemDto.of("빅조브 성인", "35,000원")
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

            Spot spot81 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(Activity.getId())
                            .name("코리아 짚와이어")
                            .address("경남 하동군 금남면 경충로 493-37")
                            .contact("055-884-7715")
                            .hashtags(
                                    List.of(
                                            "짚라인",
                                            "하동짚라인"
                                    )
                            )
                            .menuItems(
                                    List.of(
                                            MenuItemDto.of("대인", "40,000원"),
                                            MenuItemDto.of("청소년", "35,000원"),
                                            MenuItemDto.of("소인", "30,000원")
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

            Spot spot82 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(hotelAndResort.getId())
                            .name("파라다이스 호텔 부산")
                            .address("부산 해운대구 해운대해변로 296")
                            .contact("051-742-2121")
                            .hashtags(
                                    List.of(
                                            "5성급",
                                            "오션뷰"
                                    )
                            )
                            .menuItems(
                                    List.of(
                                            MenuItemDto.of("본관시티트윈", "508,200원"),
                                            MenuItemDto.of("신관시티트윈", "532,400원"),
                                            MenuItemDto.of("신관오션테라스더블", "653,400원")
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

            Spot spot83 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(hotelAndResort.getId())
                            .name("서울신라호텔")
                            .address("서울 중구 동호로 249")
                            .contact("02-2233-3131")
                            .hashtags(
                                    List.of(
                                            "실내수영장",
                                            "스파"
                                    )
                            )
                            .menuItems(
                                    List.of(
                                            MenuItemDto.of("디럭스 더블 룸(실내 수영장 이용 포함)", "550,000원"),
                                            MenuItemDto.of("디럭스 더블룸 (야외 수영장 이용 불포함)", "540,000원"),
                                            MenuItemDto.of("그랜드 코너 디럭스룸 - 실내 수영장 이용만 포함", "715,000원")
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

            Spot spot84 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(hotelAndResort.getId())
                            .name("시그니엘 서울")
                            .address("서울 송파구 올림픽로 300")
                            .contact("02-3213-1000")
                            .hashtags(
                                    List.of(
                                            "5성급",
                                            "시티뷰"
                                    )
                            )
                            .menuItems(
                                    List.of(
                                            MenuItemDto.of("그랜드 디럭스 더블", "558,000원"),
                                            MenuItemDto.of("그랜드 디럭스 (시티뷰)", "600,000원"),
                                            MenuItemDto.of("시그니엘 프리미어 더블룸", "700,000원")
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

            Spot spot85 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(Pension.getId())
                            .name("당신의안목펜션 주문진점")
                            .address("강원 강릉시 주문진읍 등대길 39")
                            .contact("010-8743-8682")
                            .hashtags(
                                    List.of(
                                            "스파",
                                            "바닷가"
                                    )
                            )
                            .menuItems(
                                    List.of(
                                            MenuItemDto.of("2인", "220,000원"),
                                            MenuItemDto.of("3인", "310,000원"),
                                            MenuItemDto.of("2인(오션뷰)", "260,000원")
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

            Spot spot86 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(Pension.getId())
                            .name("뷰델카라반펜션")
                            .address("경북 경주시 감포읍 감포로 329-34")
                            .contact("0507-1375-4464")
                            .hashtags(
                                    List.of(
                                            "글램핑",
                                            "카라반"
                                    )
                            )
                            .menuItems(
                                    List.of(
                                            MenuItemDto.of("럭셔리 감성(오션뷰)", "309,00원"),
                                            MenuItemDto.of("럭셔리 일반(오션뷰)", "319,000원"),
                                            MenuItemDto.of("럭셔리 스파(오션뷰)", "329,000원")
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

            Spot spot87 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(Pension.getId())
                            .name("삼척 예그리나펜션")
                            .address("강원 삼척시 삼척로 4246-5")
                            .contact("010-8550-9001")
                            .hashtags(
                                    List.of(
                                            "펜션",
                                            "바비큐"
                                    )
                            )
                            .menuItems(
                                    List.of(
                                            MenuItemDto.of("스파 오션뷰", "230,000원"),
                                            MenuItemDto.of("스파 오션뷰(3인)", "290,000원"),
                                            MenuItemDto.of("그랜드투룸 오션뷰", "410,000원")
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

            Spot spot88 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(poolVilla.getId())
                            .name("더강문 풀빌라&스파")
                            .address("강원 강릉시 해안로 376")
                            .contact("010-9647-9456")
                            .hashtags(
                                    List.of(
                                            "펜션",
                                            "풀빌라"
                                    )
                            )
                            .menuItems(
                                    List.of(
                                            MenuItemDto.of("2인", "434,100원"),
                                            MenuItemDto.of("3인", "470,100원"),
                                            MenuItemDto.of("4인", "614,000원")
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

            Spot spot89 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(poolVilla.getId())
                            .name("수피아라 풀빌라")
                            .address("경북 경주시 감포읍 동해안로 2647")
                            .contact("1833-6682")
                            .hashtags(
                                    List.of(
                                            "풀빌라",
                                            "바다"
                                    )
                            )
                            .menuItems(
                                    List.of(
                                            MenuItemDto.of("특가", "219,000원"),
                                            MenuItemDto.of("특가(스파)", "229,000원"),
                                            MenuItemDto.of("풀빌라", "339,000원")
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

            Spot spot90 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(poolVilla.getId())
                            .name("아르뷰 풀빌라 펜션")
                            .address("경남 통영시 도산면 저산유촌길 40 아르뷰 풀빌라펜션")
                            .contact("1833-6682")
                            .hashtags(
                                    List.of(
                                            "풀빌라",
                                            "온수풀"
                                    )
                            )
                            .menuItems(
                                    List.of(
                                            MenuItemDto.of("독채/풀빌라", "429,000원"),
                                            MenuItemDto.of("독채/풀빌라", "429,000원"),
                                            MenuItemDto.of("독채/프리미엄풀빌라", "459,000원")
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

            Spot spot91 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(familyStyle.getId())
                            .name("리틀비키즈풀빌라펜션")
                            .address("경기 가평군 북면 가화로 1310-14 리틀비키즈풀빌라(구 라망펜션)")
                            .contact("0507-1341-2296")
                            .hashtags(
                                    List.of(
                                            "가성비",
                                            "키즈"
                                    )
                            )
                            .menuItems(
                                    List.of(
                                            MenuItemDto.of("정글방", "80,000원"),
                                            MenuItemDto.of("생일방", "80,000원"),
                                            MenuItemDto.of("마차방", "80,000원")
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

            Spot spot92 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(familyStyle.getId())
                            .name("잠깨고키즈풀빌라펜션")
                            .address("경기 가평군 북면 논남기길 14-1")
                            .contact("0507-1438-2267")
                            .hashtags(
                                    List.of(
                                            "풀빌라",
                                            "워터슬라이드"
                                    )
                            )
                            .menuItems(
                                    List.of(
                                            MenuItemDto.of("가브리엘라", "220,000원"),
                                            MenuItemDto.of("프리스코", "220,000원"),
                                            MenuItemDto.of("에블린", "220,000원")
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

            Spot spot93 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(familyStyle.getId())
                            .name("까르르키즈풀빌라")
                            .address("경기 가평군 북면 카이저길 58-12 까르르키즈풀빌라펜션")
                            .contact("0507-1371-7191")
                            .hashtags(
                                    List.of(
                                            "수영장",
                                            "가족실"
                                    )
                            )
                            .menuItems(
                                    List.of(
                                            MenuItemDto.of("KKARR D-2", "800,000원"),
                                            MenuItemDto.of("KKARR B", "1,100,000원"),
                                            MenuItemDto.of("KKARR A", "1,300,000원")
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

            Spot spot94 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(motel.getId())
                            .name("유성 스파타워")
                            .address("대전 유성구 온천로107번길 40 스파타워 모텔")
                            .contact("0507-1324-0637")
                            .hashtags(
                                    List.of(
                                            "온천수",
                                            "스파"
                                    )
                            )
                            .menuItems(
                                    List.of(
                                            MenuItemDto.of("스탠다드", "100,000원"),
                                            MenuItemDto.of("프리미엄", "110,000원"),
                                            MenuItemDto.of("대형스파특실", "130,000원")
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

            Spot spot95 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(motel.getId())
                            .name("유성 세르보")
                            .address("대전 유성구 계룡로155번길 14")
                            .contact("0507-1428-1350")
                            .hashtags(
                                    List.of(
                                            "족욕체험장부근",
                                            "시설"
                                    )
                            )
                            .menuItems(
                                    List.of(
                                            MenuItemDto.of("프리미엄", "110,000원"),
                                            MenuItemDto.of("스위트", "150,000원"),
                                            MenuItemDto.of("이벤트 무한대실", "50,000원")
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

            Spot spot96 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(motel.getId())
                            .name("YAM 대전유성온천점")
                            .address("대전 유성구 온천서로 30-13")
                            .contact("0507-1362-8000")
                            .hashtags(
                                    List.of(
                                            "커플PC",
                                            "조식제공"
                                    )
                            )
                            .menuItems(
                                    List.of(
                                            MenuItemDto.of("스탠다드", "90,000원"),
                                            MenuItemDto.of("프리미엄", "95,000원"),
                                            MenuItemDto.of("VIP", "100,000원")
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

            Spot spot97 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(camping.getId())
                            .name("뉴욕카라반")
                            .address("강원 속초시 동해대로 3847-7")
                            .contact("010-2929-9670")
                            .hashtags(
                                    List.of(
                                            "캠핑",
                                            "야영장"
                                    )
                            )
                            .menuItems(
                                    List.of(
                                            MenuItemDto.of("202호", "169,000원"),
                                            MenuItemDto.of("204호", "179,000원"),
                                            MenuItemDto.of("205호", "199,000원")
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

            Spot spot98 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(camping.getId())
                            .name("청포대썬셋카라반")
                            .address("충남 태안군 남면 안면대로 1296-53")
                            .contact("0507-1365-8492")
                            .hashtags(
                                    List.of(
                                            "캠핑",
                                            "펜션"
                                    )
                            )
                            .menuItems(
                                    List.of(
                                            MenuItemDto.of("카라반파크", "279,000원"),
                                            MenuItemDto.of("카라반파크", "279,000원"),
                                            MenuItemDto.of("카라반파크", "279,000원")
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

            Spot spot99 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(camping.getId())
                            .name("용장글램핑")
                            .address("경북 경주시 내남면 용장1길 18 용장글램핑")
                            .contact("010-3508-9287")
                            .hashtags(
                                    List.of(
                                            "캠핑",
                                            "야영장"
                                    )
                            )
                            .menuItems(
                                    List.of(
                                            MenuItemDto.of("한글", "170,000원"),
                                            MenuItemDto.of("큐브", "220,000원"),
                                            MenuItemDto.of("윈드", "220,000원")
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

            Spot spot100 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(guesthouse.getId())
                            .name("한옥고택 고을")
                            .address("전북 전주시 완산구 최명희길 26-7")
                            .contact("010-2167-1188")
                            .hashtags(
                                    List.of(
                                            "전통숙소",
                                            "한옥"
                                    )
                            )
                            .menuItems(
                                    List.of(
                                            MenuItemDto.of("희망", "45,000원"),
                                            MenuItemDto.of("소망", "55,000원"),
                                            MenuItemDto.of("청명", "60,000원")
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

            Spot spot101 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(guesthouse.getId())
                            .name("빅오쇼게스트하우스")
                            .address("전남 여수시 공화북5길 8")
                            .contact("061-664-5552")
                            .hashtags(
                                    List.of(
                                            "게스트하우스",
                                            "신식"
                                    )
                            )
                            .menuItems(
                                    List.of(
                                            MenuItemDto.of("4인여자도미토리", "30,000원"),
                                            MenuItemDto.of("4인남자도미토리", "30,000원"),
                                            MenuItemDto.of("4인가족룸", "100,000원")
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

            Spot spot102 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(guesthouse.getId())
                            .name("네모 스테이")
                            .address("제주 제주시 한림읍 한림로 394 1층~3층")
                            .contact("0507-1313-0946")
                            .hashtags(
                                    List.of(
                                            "게스트하우스",
                                            "남녀분리"
                                    )
                            )
                            .menuItems(
                                    List.of(
                                            MenuItemDto.of("여성 도미토리", "25,000원"),
                                            MenuItemDto.of("남성 도미토리", "35,000원"),
                                            MenuItemDto.of("슈퍼싱글", "70,000원")
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

            Review
                    review1
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot1.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트1")
                                    .content("test1")
                                    .score(1.0)
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

            Review
                    review2
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot1.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트2")
                                    .content("test2")
                                    .score(1.5)
                                    .hashtags(
                                            List.of(
                                                    "해시태그2",
                                                    "해시태그3"
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

            Review
                    review3
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot2.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트3")
                                    .content("test3")
                                    .score(2.0)
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
                            AppConfig.toUser(user1)
                    );

            Review
                    review4
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot2.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트4")
                                    .content("test4")
                                    .score(2.5)
                                    .hashtags(
                                            List.of(
                                                    "해시태그4",
                                                    "해시태그5"
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

            Review
                    review5
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot3.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트5")
                                    .content("test5")
                                    .score(3.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그5",
                                                    "해시태그6"
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

            Review
                    review6
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot3.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트6")
                                    .content("test6")
                                    .score(3.5)
                                    .hashtags(
                                            List.of(
                                                    "해시태그6",
                                                    "해시태그7"
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

            Review
                    review7
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot4.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트7")
                                    .content("test7")
                                    .score(4.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그7",
                                                    "해시태그8"
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

            Review
                    review8
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot4.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트8")
                                    .content("test8")
                                    .score(4.5)
                                    .hashtags(
                                            List.of(
                                                    "해시태그8",
                                                    "해시태그9"
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

            Review
                    review9
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot5.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트9")
                                    .content("test9")
                                    .score(5.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그9",
                                                    "해시태그10"
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

            Review
                    review10
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot5.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트10")
                                    .content("test10")
                                    .score(1.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그10",
                                                    "해시태그11"
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

            Review
                    review11
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot6.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트11")
                                    .content("test11")
                                    .score(1.5)
                                    .hashtags(
                                            List.of(
                                                    "해시태그11",
                                                    "해시태그12"
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

            Review
                    review12
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot6.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트12")
                                    .content("test12")
                                    .score(2.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그12",
                                                    "해시태그13"
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

            Review
                    review13
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot7.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트13")
                                    .content("test13")
                                    .score(2.5)
                                    .hashtags(
                                            List.of(
                                                    "해시태그13",
                                                    "해시태그14"
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

            Review
                    review14
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot7.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트14")
                                    .content("test14")
                                    .score(3.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그14",
                                                    "해시태그15"
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

            Review
                    review15
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot8.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트15")
                                    .content("test15")
                                    .score(3.5)
                                    .hashtags(
                                            List.of(
                                                    "해시태그15",
                                                    "해시태그16"
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

            Review
                    review16
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot8.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트16")
                                    .content("test16")
                                    .score(4.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그16",
                                                    "해시태그17"
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

            Review
                    review17
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot9.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트17")
                                    .content("test17")
                                    .score(4.5)
                                    .hashtags(
                                            List.of(
                                                    "해시태그17",
                                                    "해시태그18"
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

            Review
                    review18
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot9.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트18")
                                    .content("test18")
                                    .score(5.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그18",
                                                    "해시태그19"
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

            Review
                    review19
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot10.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트19")
                                    .content("test19")
                                    .score(1.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그19",
                                                    "해시태그20"
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

            Review
                    review20
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot10.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트20")
                                    .content("test20")
                                    .score(1.5)
                                    .hashtags(
                                            List.of(
                                                    "해시태그20",
                                                    "해시태그21"
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

            Review
                    review21
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot11.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트21")
                                    .content("test21")
                                    .score(2.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그21",
                                                    "해시태그22"
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

            Review
                    review22
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot11.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트22")
                                    .content("test22")
                                    .score(2.5)
                                    .hashtags(
                                            List.of(
                                                    "해시태그22",
                                                    "해시태그23"
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

            Review
                    review23
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot12.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트23")
                                    .content("test23")
                                    .score(3.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그23",
                                                    "해시태그24"
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

            Review
                    review24
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot12.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트24")
                                    .content("test24")
                                    .score(3.5)
                                    .hashtags(
                                            List.of(
                                                    "해시태그24",
                                                    "해시태그25"
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

            Review
                    review25
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot13.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트25")
                                    .content("test25")
                                    .score(4.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그25",
                                                    "해시태그26"
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

            Review
                    review26
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot13.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트26")
                                    .content("test26")
                                    .score(4.5)
                                    .hashtags(
                                            List.of(
                                                    "해시태그26",
                                                    "해시태그27"
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

            Review
                    review27
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot14.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트27")
                                    .content("test27")
                                    .score(5.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그27",
                                                    "해시태그28"
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

            Review
                    review28
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot14.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트28")
                                    .content("test28")
                                    .score(1.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그28",
                                                    "해시태그29"
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

            Review
                    review29
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot15.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트29")
                                    .content("test29")
                                    .score(1.5)
                                    .hashtags(
                                            List.of(
                                                    "해시태그29",
                                                    "해시태그30"
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

            Review
                    review30
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot15.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트30")
                                    .content("test30")
                                    .score(2.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그30",
                                                    "해시태그31"
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

            Review
                    review31
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot16.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트31")
                                    .content("test31")
                                    .score(2.5)
                                    .hashtags(
                                            List.of(
                                                    "해시태그31",
                                                    "해시태그32"
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

            Review
                    review32
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot16.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트32")
                                    .content("test32")
                                    .score(3.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그32",
                                                    "해시태그33"
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

            Review
                    review33
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot17.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트33")
                                    .content("test33")
                                    .score(3.5)
                                    .hashtags(
                                            List.of(
                                                    "해시태그33",
                                                    "해시태그34"
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

            Review
                    review34
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot17.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트34")
                                    .content("test34")
                                    .score(4.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그34",
                                                    "해시태그35"
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

            Review
                    review35
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot18.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트35")
                                    .content("test35")
                                    .score(4.5)
                                    .hashtags(
                                            List.of(
                                                    "해시태그35",
                                                    "해시태그36"
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

            Review
                    review36
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot18.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트36")
                                    .content("test36")
                                    .score(5.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그36",
                                                    "해시태그37"
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

            Review
                    review37
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot19.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트37")
                                    .content("test37")
                                    .score(1.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그37",
                                                    "해시태그38"
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

            Review
                    review38
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot19.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트38")
                                    .content("test38")
                                    .score(1.5)
                                    .hashtags(
                                            List.of(
                                                    "해시태그38",
                                                    "해시태그39"
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

            Review
                    review39
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot20.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트39")
                                    .content("test39")
                                    .score(2.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그39",
                                                    "해시태그40"
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

            Review
                    review40
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot20.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트40")
                                    .content("test40")
                                    .score(2.5)
                                    .hashtags(
                                            List.of(
                                                    "해시태그40",
                                                    "해시태그41"
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

            Review
                    review41
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot21.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트41")
                                    .content("test41")
                                    .score(3.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그41",
                                                    "해시태그42"
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

            Review
                    review42
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot21.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트42")
                                    .content("test42")
                                    .score(3.5)
                                    .hashtags(
                                            List.of(
                                                    "해시태그42",
                                                    "해시태그43"
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

            Review
                    review43
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot22.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트43")
                                    .content("test43")
                                    .score(4.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그43",
                                                    "해시태그44"
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

            Review
                    review44
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot22.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트44")
                                    .content("test44")
                                    .score(4.5)
                                    .hashtags(
                                            List.of(
                                                    "해시태그44",
                                                    "해시태그45"
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

            Review
                    review45
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot23.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트45")
                                    .content("test45")
                                    .score(5.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그45",
                                                    "해시태그46"
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

            Review
                    review46
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot23.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트46")
                                    .content("test46")
                                    .score(1.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그46",
                                                    "해시태그47"
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

            Review
                    review47
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot24.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트47")
                                    .content("test47")
                                    .score(1.5)
                                    .hashtags(
                                            List.of(
                                                    "해시태그47",
                                                    "해시태그48"
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

            Review
                    review48
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot24.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트48")
                                    .content("test48")
                                    .score(2.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그48",
                                                    "해시태그49"
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

            Review
                    review49
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot25.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트49")
                                    .content("test49")
                                    .score(2.5)
                                    .hashtags(
                                            List.of(
                                                    "해시태그49",
                                                    "해시태그50"
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

            Review
                    review50
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot25.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트50")
                                    .content("test50")
                                    .score(3.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그50",
                                                    "해시태그51"
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

            Review
                    review51
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot26.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트51")
                                    .content("test51")
                                    .score(3.5)
                                    .hashtags(
                                            List.of(
                                                    "해시태그51",
                                                    "해시태그52"
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

            Review
                    review52
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot26.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트52")
                                    .content("test52")
                                    .score(4.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그52",
                                                    "해시태그53"
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

            Review
                    review53
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot27.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트53")
                                    .content("test53")
                                    .score(4.5)
                                    .hashtags(
                                            List.of(
                                                    "해시태그53",
                                                    "해시태그54"
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

            Review
                    review54
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot27.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트54")
                                    .content("test54")
                                    .score(5.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그54",
                                                    "해시태그55"
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

            Review
                    review55
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot28.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트55")
                                    .content("test55")
                                    .score(1.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그55",
                                                    "해시태그56"
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

            Review
                    review56
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot28.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트56")
                                    .content("test56")
                                    .score(1.5)
                                    .hashtags(
                                            List.of(
                                                    "해시태그56",
                                                    "해시태그57"
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

            Review
                    review57
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot29.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트57")
                                    .content("test57")
                                    .score(2.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그57",
                                                    "해시태그58"
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

            Review
                    review58
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot29.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트58")
                                    .content("test58")
                                    .score(2.5)
                                    .hashtags(
                                            List.of(
                                                    "해시태그58",
                                                    "해시태그59"
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

            Review
                    review59
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot30.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트59")
                                    .content("test59")
                                    .score(3.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그59",
                                                    "해시태그60"
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

            Review
                    review60
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot30.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트60")
                                    .content("test60")
                                    .score(3.5)
                                    .hashtags(
                                            List.of(
                                                    "해시태그60",
                                                    "해시태그61"
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

            Review
                    review61
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot31.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트61")
                                    .content("test61")
                                    .score(4.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그61",
                                                    "해시태그62"
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

            Review
                    review62
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot31.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트62")
                                    .content("test62")
                                    .score(4.5)
                                    .hashtags(
                                            List.of(
                                                    "해시태그62",
                                                    "해시태그63"
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

            Review
                    review63
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot32.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트63")
                                    .content("test63")
                                    .score(5.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그63",
                                                    "해시태그64"
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

            Review
                    review64
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot32.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트64")
                                    .content("test64")
                                    .score(1.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그64",
                                                    "해시태그65"
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

            Review
                    review65
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot33.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트65")
                                    .content("test65")
                                    .score(1.5)
                                    .hashtags(
                                            List.of(
                                                    "해시태그65",
                                                    "해시태그66"
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

            Review
                    review66
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot33.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트66")
                                    .content("test66")
                                    .score(2.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그66",
                                                    "해시태그67"
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

            Review
                    review67
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot34.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트67")
                                    .content("test67")
                                    .score(2.5)
                                    .hashtags(
                                            List.of(
                                                    "해시태그67",
                                                    "해시태그68"
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

            Review
                    review68
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot34.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트68")
                                    .content("test68")
                                    .score(3.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그68",
                                                    "해시태그69"
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

            Review
                    review69
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot35.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트69")
                                    .content("test69")
                                    .score(3.5)
                                    .hashtags(
                                            List.of(
                                                    "해시태그69",
                                                    "해시태그70"
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

            Review
                    review70
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot35.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트70")
                                    .content("test70")
                                    .score(4.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그70",
                                                    "해시태그71"
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

            Review
                    review71
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot36.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트71")
                                    .content("test71")
                                    .score(4.5)
                                    .hashtags(
                                            List.of(
                                                    "해시태그71",
                                                    "해시태그72"
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

            Review
                    review72
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot36.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트72")
                                    .content("test72")
                                    .score(5.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그72",
                                                    "해시태그73"
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

            Review
                    review73
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot37.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트73")
                                    .content("test73")
                                    .score(1.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그73",
                                                    "해시태그74"
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

            Review
                    review74
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot37.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트74")
                                    .content("test74")
                                    .score(1.5)
                                    .hashtags(
                                            List.of(
                                                    "해시태그74",
                                                    "해시태그75"
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

            Review
                    review75
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot38.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트75")
                                    .content("test75")
                                    .score(2.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그75",
                                                    "해시태그76"
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

            Review
                    review76
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot38.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트76")
                                    .content("test76")
                                    .score(2.5)
                                    .hashtags(
                                            List.of(
                                                    "해시태그76",
                                                    "해시태그77"
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

            Review
                    review77
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot39.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트77")
                                    .content("test77")
                                    .score(3.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그77",
                                                    "해시태그78"
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

            Review
                    review78
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot39.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트78")
                                    .content("test78")
                                    .score(3.5)
                                    .hashtags(
                                            List.of(
                                                    "해시태그78",
                                                    "해시태그79"
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

            Review
                    review79
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot40.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트79")
                                    .content("test79")
                                    .score(4.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그79",
                                                    "해시태그80"
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

            Review
                    review80
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot40.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트80")
                                    .content("test80")
                                    .score(4.5)
                                    .hashtags(
                                            List.of(
                                                    "해시태그80",
                                                    "해시태그81"
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

            Review
                    review81
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot41.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트81")
                                    .content("test81")
                                    .score(5.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그81",
                                                    "해시태그82"
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

            Review
                    review82
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot41.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트82")
                                    .content("test82")
                                    .score(1.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그82",
                                                    "해시태그83"
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

            Review
                    review83
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot42.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트83")
                                    .content("test83")
                                    .score(1.5)
                                    .hashtags(
                                            List.of(
                                                    "해시태그83",
                                                    "해시태그84"
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

            Review
                    review84
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot42.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트84")
                                    .content("test84")
                                    .score(2.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그84",
                                                    "해시태그85"
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

            Review
                    review85
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot43.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트85")
                                    .content("test85")
                                    .score(2.5)
                                    .hashtags(
                                            List.of(
                                                    "해시태그85",
                                                    "해시태그86"
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

            Review
                    review86
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot43.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트86")
                                    .content("test86")
                                    .score(3.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그86",
                                                    "해시태그87"
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

            Review
                    review87
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot44.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트87")
                                    .content("test87")
                                    .score(3.5)
                                    .hashtags(
                                            List.of(
                                                    "해시태그87",
                                                    "해시태그88"
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

            Review
                    review88
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot44.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트88")
                                    .content("test88")
                                    .score(4.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그88",
                                                    "해시태그89"
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

            Review
                    review89
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot45.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트89")
                                    .content("test89")
                                    .score(4.5)
                                    .hashtags(
                                            List.of(
                                                    "해시태그89",
                                                    "해시태그90"
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

            Review
                    review90
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot45.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트90")
                                    .content("test90")
                                    .score(5.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그90",
                                                    "해시태그91"
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

            Review
                    review91
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot46.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트91")
                                    .content("test91")
                                    .score(1.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그91",
                                                    "해시태그92"
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

            Review
                    review92
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot46.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트92")
                                    .content("test92")
                                    .score(1.5)
                                    .hashtags(
                                            List.of(
                                                    "해시태그92",
                                                    "해시태그93"
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

            Review
                    review93
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot47.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트93")
                                    .content("test93")
                                    .score(2.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그93",
                                                    "해시태그94"
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

            Review
                    review94
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot47.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트94")
                                    .content("test94")
                                    .score(2.5)
                                    .hashtags(
                                            List.of(
                                                    "해시태그94",
                                                    "해시태그95"
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

            Review
                    review95
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot48.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트95")
                                    .content("test95")
                                    .score(3.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그95",
                                                    "해시태그96"
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

            Review
                    review96
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot48.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트96")
                                    .content("test96")
                                    .score(3.5)
                                    .hashtags(
                                            List.of(
                                                    "해시태그96",
                                                    "해시태그97"
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

            Review
                    review97
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot49.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트97")
                                    .content("test97")
                                    .score(4.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그97",
                                                    "해시태그98"
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

            Review
                    review98
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot49.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트98")
                                    .content("test98")
                                    .score(4.5)
                                    .hashtags(
                                            List.of(
                                                    "해시태그98",
                                                    "해시태그99"
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

            Review
                    review99
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot50.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트99")
                                    .content("test99")
                                    .score(5.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그99",
                                                    "해시태그100"
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

            Review
                    review100
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot50.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트100")
                                    .content("test100")
                                    .score(1.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그100",
                                                    "해시태그101"
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

            Review
                    review101
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot51.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트101")
                                    .content("test101")
                                    .score(1.5)
                                    .hashtags(
                                            List.of(
                                                    "해시태그101",
                                                    "해시태그102"
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

            Review
                    review102
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot51.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트102")
                                    .content("test102")
                                    .score(2.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그102",
                                                    "해시태그103"
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

            Review
                    review103
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot52.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트103")
                                    .content("test103")
                                    .score(2.5)
                                    .hashtags(
                                            List.of(
                                                    "해시태그103",
                                                    "해시태그104"
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

            Review
                    review104
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot52.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트104")
                                    .content("test104")
                                    .score(3.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그104",
                                                    "해시태그105"
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

            Review
                    review105
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot53.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트105")
                                    .content("test105")
                                    .score(3.5)
                                    .hashtags(
                                            List.of(
                                                    "해시태그105",
                                                    "해시태그106"
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

            Review
                    review106
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot53.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트106")
                                    .content("test106")
                                    .score(4.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그106",
                                                    "해시태그107"
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

            Review
                    review107
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot54.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트107")
                                    .content("test107")
                                    .score(4.5)
                                    .hashtags(
                                            List.of(
                                                    "해시태그107",
                                                    "해시태그108"
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

            Review
                    review108
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot54.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트108")
                                    .content("test108")
                                    .score(5.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그108",
                                                    "해시태그109"
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

            Review
                    review109
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot55.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트109")
                                    .content("test109")
                                    .score(1.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그109",
                                                    "해시태그110"
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

            Review
                    review110
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot55.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트110")
                                    .content("test110")
                                    .score(1.5)
                                    .hashtags(
                                            List.of(
                                                    "해시태그110",
                                                    "해시태그111"
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

            Review
                    review111
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot56.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트111")
                                    .content("test111")
                                    .score(2.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그111",
                                                    "해시태그112"
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

            Review
                    review112
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot56.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트112")
                                    .content("test112")
                                    .score(2.5)
                                    .hashtags(
                                            List.of(
                                                    "해시태그112",
                                                    "해시태그113"
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

            Review
                    review113
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot57.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트113")
                                    .content("test113")
                                    .score(3.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그113",
                                                    "해시태그114"
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

            Review
                    review114
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot57.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트114")
                                    .content("test114")
                                    .score(3.5)
                                    .hashtags(
                                            List.of(
                                                    "해시태그114",
                                                    "해시태그115"
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

            Review
                    review115
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot58.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트115")
                                    .content("test115")
                                    .score(4.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그115",
                                                    "해시태그116"
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

            Review
                    review116
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot58.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트116")
                                    .content("test116")
                                    .score(4.5)
                                    .hashtags(
                                            List.of(
                                                    "해시태그116",
                                                    "해시태그117"
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

            Review
                    review117
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot59.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트117")
                                    .content("test117")
                                    .score(5.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그117",
                                                    "해시태그118"
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

            Review
                    review118
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot59.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트118")
                                    .content("test118")
                                    .score(1.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그118",
                                                    "해시태그119"
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

            Review
                    review119
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot60.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트119")
                                    .content("test119")
                                    .score(1.5)
                                    .hashtags(
                                            List.of(
                                                    "해시태그119",
                                                    "해시태그120"
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

            Review
                    review120
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot60.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트120")
                                    .content("test120")
                                    .score(2.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그120",
                                                    "해시태그121"
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

            Review
                    review121
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot61.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트121")
                                    .content("test121")
                                    .score(2.5)
                                    .hashtags(
                                            List.of(
                                                    "해시태그121",
                                                    "해시태그122"
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

            Review
                    review122
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot61.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트122")
                                    .content("test122")
                                    .score(3.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그122",
                                                    "해시태그123"
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

            Review
                    review123
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot62.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트123")
                                    .content("test123")
                                    .score(3.5)
                                    .hashtags(
                                            List.of(
                                                    "해시태그123",
                                                    "해시태그124"
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

            Review
                    review124
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot62.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트124")
                                    .content("test124")
                                    .score(4.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그124",
                                                    "해시태그125"
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

            Review
                    review125
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot63.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트125")
                                    .content("test125")
                                    .score(4.5)
                                    .hashtags(
                                            List.of(
                                                    "해시태그125",
                                                    "해시태그126"
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

            Review
                    review126
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot63.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트126")
                                    .content("test126")
                                    .score(5.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그126",
                                                    "해시태그127"
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

            Review
                    review127
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot64.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트127")
                                    .content("test127")
                                    .score(1.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그127",
                                                    "해시태그128"
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

            Review
                    review128
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot64.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트128")
                                    .content("test128")
                                    .score(1.5)
                                    .hashtags(
                                            List.of(
                                                    "해시태그128",
                                                    "해시태그129"
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

            Review
                    review129
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot65.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트129")
                                    .content("test129")
                                    .score(2.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그129",
                                                    "해시태그130"
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

            Review
                    review130
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot65.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트130")
                                    .content("test130")
                                    .score(2.5)
                                    .hashtags(
                                            List.of(
                                                    "해시태그130",
                                                    "해시태그131"
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

            Review
                    review131
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot66.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트131")
                                    .content("test131")
                                    .score(3.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그131",
                                                    "해시태그132"
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

            Review
                    review132
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot66.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트132")
                                    .content("test132")
                                    .score(3.5)
                                    .hashtags(
                                            List.of(
                                                    "해시태그132",
                                                    "해시태그133"
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

            Review
                    review133
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot67.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트133")
                                    .content("test133")
                                    .score(4.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그133",
                                                    "해시태그134"
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

            Review
                    review134
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot67.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트134")
                                    .content("test134")
                                    .score(4.5)
                                    .hashtags(
                                            List.of(
                                                    "해시태그134",
                                                    "해시태그135"
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

            Review
                    review135
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot68.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트135")
                                    .content("test135")
                                    .score(5.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그135",
                                                    "해시태그136"
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

            Review
                    review136
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot68.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트136")
                                    .content("test136")
                                    .score(1.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그136",
                                                    "해시태그137"
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

            Review
                    review137
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot69.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트137")
                                    .content("test137")
                                    .score(1.5)
                                    .hashtags(
                                            List.of(
                                                    "해시태그137",
                                                    "해시태그138"
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

            Review
                    review138
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot69.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트138")
                                    .content("test138")
                                    .score(2.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그138",
                                                    "해시태그139"
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

            Review
                    review139
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot70.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트139")
                                    .content("test139")
                                    .score(2.5)
                                    .hashtags(
                                            List.of(
                                                    "해시태그139",
                                                    "해시태그140"
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

            Review
                    review140
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot70.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트140")
                                    .content("test140")
                                    .score(3.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그140",
                                                    "해시태그141"
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

            Review
                    review141
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot71.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트141")
                                    .content("test141")
                                    .score(3.5)
                                    .hashtags(
                                            List.of(
                                                    "해시태그141",
                                                    "해시태그142"
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

            Review
                    review142
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot71.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트142")
                                    .content("test142")
                                    .score(4.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그142",
                                                    "해시태그143"
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

            Review
                    review143
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot72.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트143")
                                    .content("test143")
                                    .score(4.5)
                                    .hashtags(
                                            List.of(
                                                    "해시태그143",
                                                    "해시태그144"
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

            Review
                    review144
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot72.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트144")
                                    .content("test144")
                                    .score(5.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그144",
                                                    "해시태그145"
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

            Review
                    review145
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot73.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트145")
                                    .content("test145")
                                    .score(1.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그145",
                                                    "해시태그146"
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

            Review
                    review146
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot73.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트146")
                                    .content("test146")
                                    .score(1.5)
                                    .hashtags(
                                            List.of(
                                                    "해시태그146",
                                                    "해시태그147"
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

            Review
                    review147
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot74.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트147")
                                    .content("test147")
                                    .score(2.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그147",
                                                    "해시태그148"
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

            Review
                    review148
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot74.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트148")
                                    .content("test148")
                                    .score(2.5)
                                    .hashtags(
                                            List.of(
                                                    "해시태그148",
                                                    "해시태그149"
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

            Review
                    review149
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot75.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트149")
                                    .content("test149")
                                    .score(3.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그149",
                                                    "해시태그150"
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

            Review
                    review150
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot75.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트150")
                                    .content("test150")
                                    .score(3.5)
                                    .hashtags(
                                            List.of(
                                                    "해시태그150",
                                                    "해시태그151"
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

            Review
                    review151
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot76.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트151")
                                    .content("test151")
                                    .score(4.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그151",
                                                    "해시태그152"
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

            Review
                    review152
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot76.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트152")
                                    .content("test152")
                                    .score(4.5)
                                    .hashtags(
                                            List.of(
                                                    "해시태그152",
                                                    "해시태그153"
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

            Review
                    review153
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot77.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트153")
                                    .content("test153")
                                    .score(5.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그153",
                                                    "해시태그154"
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

            Review
                    review154
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot77.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트154")
                                    .content("test154")
                                    .score(1.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그154",
                                                    "해시태그155"
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

            Review
                    review155
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot78.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트155")
                                    .content("test155")
                                    .score(1.5)
                                    .hashtags(
                                            List.of(
                                                    "해시태그155",
                                                    "해시태그156"
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

            Review
                    review156
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot78.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트156")
                                    .content("test156")
                                    .score(2.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그156",
                                                    "해시태그157"
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

            Review
                    review157
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot79.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트157")
                                    .content("test157")
                                    .score(2.5)
                                    .hashtags(
                                            List.of(
                                                    "해시태그157",
                                                    "해시태그158"
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

            Review
                    review158
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot79.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트158")
                                    .content("test158")
                                    .score(3.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그158",
                                                    "해시태그159"
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

            Review
                    review159
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot80.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트159")
                                    .content("test159")
                                    .score(3.5)
                                    .hashtags(
                                            List.of(
                                                    "해시태그159",
                                                    "해시태그160"
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

            Review
                    review160
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot80.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트160")
                                    .content("test160")
                                    .score(4.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그160",
                                                    "해시태그161"
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

            Review
                    review161
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot81.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트161")
                                    .content("test161")
                                    .score(4.5)
                                    .hashtags(
                                            List.of(
                                                    "해시태그161",
                                                    "해시태그162"
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

            Review
                    review162
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot81.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트162")
                                    .content("test162")
                                    .score(5.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그162",
                                                    "해시태그163"
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

            Review
                    review163
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot82.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트163")
                                    .content("test163")
                                    .score(1.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그163",
                                                    "해시태그164"
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

            Review
                    review164
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot82.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트164")
                                    .content("test164")
                                    .score(1.5)
                                    .hashtags(
                                            List.of(
                                                    "해시태그164",
                                                    "해시태그165"
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

            Review
                    review165
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot83.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트165")
                                    .content("test165")
                                    .score(2.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그165",
                                                    "해시태그166"
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

            Review
                    review166
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot83.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트166")
                                    .content("test166")
                                    .score(2.5)
                                    .hashtags(
                                            List.of(
                                                    "해시태그166",
                                                    "해시태그167"
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

            Review
                    review167
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot84.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트167")
                                    .content("test167")
                                    .score(3.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그167",
                                                    "해시태그168"
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

            Review
                    review168
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot84.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트168")
                                    .content("test168")
                                    .score(3.5)
                                    .hashtags(
                                            List.of(
                                                    "해시태그168",
                                                    "해시태그169"
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

            Review
                    review169
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot85.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트169")
                                    .content("test169")
                                    .score(4.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그169",
                                                    "해시태그170"
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

            Review
                    review170
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot85.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트170")
                                    .content("test170")
                                    .score(4.5)
                                    .hashtags(
                                            List.of(
                                                    "해시태그170",
                                                    "해시태그171"
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

            Review
                    review171
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot86.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트171")
                                    .content("test171")
                                    .score(5.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그171",
                                                    "해시태그172"
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

            Review
                    review172
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot86.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트172")
                                    .content("test172")
                                    .score(1.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그172",
                                                    "해시태그173"
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

            Review
                    review173
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot87.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트173")
                                    .content("test173")
                                    .score(1.5)
                                    .hashtags(
                                            List.of(
                                                    "해시태그173",
                                                    "해시태그174"
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

            Review
                    review174
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot87.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트174")
                                    .content("test174")
                                    .score(2.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그174",
                                                    "해시태그175"
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

            Review
                    review175
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot88.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트175")
                                    .content("test175")
                                    .score(2.5)
                                    .hashtags(
                                            List.of(
                                                    "해시태그175",
                                                    "해시태그176"
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

            Review
                    review176
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot88.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트176")
                                    .content("test176")
                                    .score(3.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그176",
                                                    "해시태그177"
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

            Review
                    review177
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot89.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트177")
                                    .content("test177")
                                    .score(3.5)
                                    .hashtags(
                                            List.of(
                                                    "해시태그177",
                                                    "해시태그178"
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

            Review
                    review178
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot89.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트178")
                                    .content("test178")
                                    .score(4.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그178",
                                                    "해시태그179"
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

            Review
                    review179
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot90.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트179")
                                    .content("test179")
                                    .score(4.5)
                                    .hashtags(
                                            List.of(
                                                    "해시태그179",
                                                    "해시태그180"
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

            Review
                    review180
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot90.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트180")
                                    .content("test180")
                                    .score(5.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그180",
                                                    "해시태그181"
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

            Review
                    review181
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot91.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트181")
                                    .content("test181")
                                    .score(1.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그181",
                                                    "해시태그182"
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

            Review
                    review182
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot91.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트182")
                                    .content("test182")
                                    .score(1.5)
                                    .hashtags(
                                            List.of(
                                                    "해시태그182",
                                                    "해시태그183"
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

            Review
                    review183
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot92.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트183")
                                    .content("test183")
                                    .score(2.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그183",
                                                    "해시태그184"
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

            Review
                    review184
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot92.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트184")
                                    .content("test184")
                                    .score(2.5)
                                    .hashtags(
                                            List.of(
                                                    "해시태그184",
                                                    "해시태그185"
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

            Review
                    review185
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot93.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트185")
                                    .content("test185")
                                    .score(3.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그185",
                                                    "해시태그186"
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

            Review
                    review186
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot93.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트186")
                                    .content("test186")
                                    .score(3.5)
                                    .hashtags(
                                            List.of(
                                                    "해시태그186",
                                                    "해시태그187"
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

            Review
                    review187
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot94.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트187")
                                    .content("test187")
                                    .score(4.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그187",
                                                    "해시태그188"
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

            Review
                    review188
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot94.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트188")
                                    .content("test188")
                                    .score(4.5)
                                    .hashtags(
                                            List.of(
                                                    "해시태그188",
                                                    "해시태그189"
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

            Review
                    review189
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot95.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트189")
                                    .content("test189")
                                    .score(5.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그189",
                                                    "해시태그190"
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

            Review
                    review190
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot95.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트190")
                                    .content("test190")
                                    .score(1.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그190",
                                                    "해시태그191"
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

            Review
                    review191
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot96.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트191")
                                    .content("test191")
                                    .score(1.5)
                                    .hashtags(
                                            List.of(
                                                    "해시태그191",
                                                    "해시태그192"
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

            Review
                    review192
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot96.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트192")
                                    .content("test192")
                                    .score(2.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그192",
                                                    "해시태그193"
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

            Review
                    review193
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot97.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트193")
                                    .content("test193")
                                    .score(2.5)
                                    .hashtags(
                                            List.of(
                                                    "해시태그193",
                                                    "해시태그194"
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

            Review
                    review194
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot97.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트194")
                                    .content("test194")
                                    .score(3.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그194",
                                                    "해시태그195"
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

            Review
                    review195
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot98.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트195")
                                    .content("test195")
                                    .score(3.5)
                                    .hashtags(
                                            List.of(
                                                    "해시태그195",
                                                    "해시태그196"
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

            Review
                    review196
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot98.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트196")
                                    .content("test196")
                                    .score(4.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그196",
                                                    "해시태그197"
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

            Review
                    review197
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot99.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트197")
                                    .content("test197")
                                    .score(4.5)
                                    .hashtags(
                                            List.of(
                                                    "해시태그197",
                                                    "해시태그198"
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

            Review
                    review198
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot99.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트198")
                                    .content("test198")
                                    .score(5.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그198",
                                                    "해시태그199"
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

            Review
                    review199
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot100.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트199")
                                    .content("test199")
                                    .score(1.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그199",
                                                    "해시태그200"
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

            Review
                    review200
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot100.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트200")
                                    .content("test200")
                                    .score(1.5)
                                    .hashtags(
                                            List.of(
                                                    "해시태그200",
                                                    "해시태그201"
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

            Review
                    review201
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot101.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트201")
                                    .content("test201")
                                    .score(2.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그201",
                                                    "해시태그202"
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

            Review
                    review202
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot101.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트202")
                                    .content("test202")
                                    .score(2.5)
                                    .hashtags(
                                            List.of(
                                                    "해시태그202",
                                                    "해시태그203"
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

            Review
                    review203
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot102.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트203")
                                    .content("test203")
                                    .score(3.0)
                                    .hashtags(
                                            List.of(
                                                    "해시태그203",
                                                    "해시태그204"
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

            Review
                    review204
                    =
                    this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(spot102.getId())
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title("테스트204")
                                    .content("test204")
                                    .score(3.5)
                                    .hashtags(
                                            List.of(
                                                    "해시태그204",
                                                    "해시태그205"
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
            Comment comment1 = this.commentService.create(
                    CommentRequest.CreateComment.builder()
                            .content("댓글내용1")
                            .reviewId(review1.getId())
                            .build(),
                    AppConfig.getMockErrors(),
                    AppConfig.toUser(user1)
            );

            Comment comment2 = this.commentService.create(
                    CommentRequest.CreateComment.builder()
                            .content("댓글내용2")
                            .reviewId(review2.getId())
                            .build(),
                    AppConfig.getMockErrors(),
                    AppConfig.toUser(user1)
            );
        };
    }
}
