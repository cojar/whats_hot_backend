package com.cojar.whats_hot_backend.global.util;

import com.cojar.whats_hot_backend.domain.base_module.file.entity.FileDomain;
import com.cojar.whats_hot_backend.domain.base_module.file.service.FileService;
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

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class InitConfig {

    private final MemberService memberService;
    private final CategoryService categoryService;
    private final SpotService spotService;
    private final ReviewService reviewService;
    private final CommentService commentService;
    private final FileService fileService;

    private final ResourceLoader resourceLoader;

    @Bean
    public ApplicationRunner runner() {
        return args -> {

            if (AppConfig.fileExists()) {
                log.info("더미 파일 삭제 실행");

                for (FileDomain fileDomain : FileDomain.values()) {

                    log.info("%s 파일 삭제 진행".formatted(fileDomain.getDomain()));

                    File folder = new File(AppConfig.getDomainFilePath(fileDomain));
                    File[] _files = folder.listFiles();

                    List<String> fileNames = this.fileService.getFilesByDomain(fileDomain).stream()
                            .map(file -> file.getUuid() + "." + file.getExt())
                            .collect(Collectors.toList());

                    int count = 0;
                    for (File _file : _files) {
                        if (!fileNames.contains(_file.getName())) {
                            _file.delete();
                            count++;
                        }
                    }

                    log.info("%s 파일 중 %s 개의 더미 파일 삭제 완료".formatted(fileDomain.getDomain(), count));
                }

                log.info("더미 파일 삭제 완료");
            }

            if (!this.memberService.hasNoMember()) {
                log.info("데이터 초기화 미실행");
                return;
            }

            log.info("데이터 초기화 실행");

            // member init data
            Map<String, Member> memberInit = new HashMap<>();

            memberInit.put("admin", this.memberService.signup(
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
            ));

            memberInit.put("user1", this.memberService.signup(
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
                    true,
                    AppConfig.getMockErrors()
            ));

            memberInit.put("user2", this.memberService.signup(
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
                    true,
                    AppConfig.getMockErrors()
            ));


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

            // image 없는 review 예시

            Random random = new Random();

            for (int i = 0; i < 2; i++) {

                List<String> randomMember = memberInit.keySet().stream()
                        .filter(memberId -> !memberId.equals("admin"))
                        .collect(Collectors.toList());

                double randomScore = 4.0 + (1.0) * random.nextDouble();
                String[] randomTitle = {"맛있습니다", "너무 좋아요~!", "추천합니다!", "다시 오고 싶어요!", "분위기 좋네요!"};
                String[] randomContent = {"추천해요~", "여러 번 와서 먹었는데, 또 오고 싶네요",
                        "가족들과 다시 오고 싶네요!", "모든 메뉴가 다 맛있네요!", "몇 번이고 추천합니다!"};
                String[] randomHashtag1 = {"혼밥", "혼밥 추천", "데이트 코스", "가족모임", "둘이서"};
                String[] randomHashtag2 = {"혼밥", "혼밥 추천", "데이트 코스", "가족모임", "둘이서"};


                String formattedScore = String.format("%.1f", randomScore);
                double score = Double.parseDouble(formattedScore);

                String selectedMember = randomMember.get(random.nextInt(randomMember.size()));
                String selectedTitle = randomTitle[random.nextInt(randomTitle.length)];
                String selectedContent = randomContent[random.nextInt(randomContent.length)];
                String selectedHashtag1 = randomHashtag1[random.nextInt(randomHashtag1.length)];
                String selectedHashtag2 = randomHashtag2[random.nextInt(randomHashtag2.length)];

                Review review = this.reviewService.create(
                        ReviewRequest.CreateReview.builder()
                                .spotId(spot1.getId())
                                .year(2024)
                                .month(1)
                                .day(1)
                                .title(selectedTitle)
                                .content(selectedContent)
                                .score(score)
                                .hashtags(List.of(
                                                selectedHashtag1,
                                                selectedHashtag2
                                        )
                                )
                                .build(),
                        List.of(),
                        AppConfig.getMockErrors(),
                        AppConfig.toUser(memberInit.get(selectedMember))
                );
            }

            // 식당 review

            for (Long i = 2L; i <= 48; i++) {

                List<String> randomMember1 = memberInit.keySet().stream()
                        .filter(memberId -> !memberId.equals("admin"))
                        .collect(Collectors.toList());

                for (int j = 0; j < 2; j++) {

                    double randomScore1 = 4.0 + (1.0) * random.nextDouble();
                    String[] randomTitle1 = {"맛있습니다", "너무 좋아요~!", "추천합니다!", "다시 오고 싶어요!", "분위기 좋네요!"};
                    String[] randomContent1 = {"추천해요~", "여러 번 와서 먹었는데, 또 오고 싶네요",
                            "가족들과 다시 오고 싶네요!", "모든 메뉴가 다 맛있네요!", "몇 번이고 추천합니다!"};
                    String[] randomHashtag3 = {"혼밥", "혼밥 추천", "데이트 코스"};
                    String[] randomHashtag4 = {"둘이서", "가족모임", "모임 가능"};

                    String formattedScore1 = String.format("%.1f", randomScore1);
                    double score1 = Double.parseDouble(formattedScore1);

                    String selectedMember1 = randomMember1.get(random.nextInt(randomMember1.size()));
                    String selectedTitle1 = randomTitle1[random.nextInt(randomTitle1.length)];
                    String selectedContent1 = randomContent1[random.nextInt(randomContent1.length)];
                    String selectedHashtag3 = randomHashtag3[random.nextInt(randomHashtag3.length)];
                    String selectedHashtag4 = randomHashtag4[random.nextInt(randomHashtag4.length)];

                    Review review1 = this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(i)
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title(selectedTitle1)
                                    .content(selectedContent1)
                                    .score(score1)
                                    .hashtags(List.of(
                                                    selectedHashtag3,
                                                    selectedHashtag4
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
                            AppConfig.toUser(memberInit.get(selectedMember1))
                    );
                }
            }

            // 여행지 review

            for (Long i = 49L; i <= 81; i++) {

                List<String> randomMember2 = memberInit.keySet().stream()
                        .filter(memberId -> !memberId.equals("admin"))
                        .collect(Collectors.toList());

                for (int j = 0; j < 2; j++) {

                    double randomScore2 = 4.0 + (1.0) * random.nextDouble();
                    String[] randomTitle2 = {"멋있어요!", "너무 좋아요~!", "추천합니다!", "다시 오고 싶어요!", "분위기 좋네요!"};
                    String[] randomContent2 = {"추천해요~", "너무 멋있네요~!",
                            "가족들과 다시 오고 싶네요!", "주변의 모든 것들이 다 예뻐요!", "몇 번이고 추천합니다!"};
                    String[] randomHashtag5 = {"가족 여행", "나홀로", "데이트 코스"};
                    String[] randomHashtag6 = {"휴가", "휴식", "둘이서"};

                    String formattedScore2 = String.format("%.1f", randomScore2);
                    double score2 = Double.parseDouble(formattedScore2);

                    String selectedMember2 = randomMember2.get(random.nextInt(randomMember2.size()));
                    String selectedTitle2 = randomTitle2[random.nextInt(randomTitle2.length)];
                    String selectedContent2 = randomContent2[random.nextInt(randomContent2.length)];
                    String selectedHashtag5 = randomHashtag5[random.nextInt(randomHashtag5.length)];
                    String selectedHashtag6 = randomHashtag6[random.nextInt(randomHashtag6.length)];

                    Review review2 = this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(i)
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title(selectedTitle2)
                                    .content(selectedContent2)
                                    .score(score2)
                                    .hashtags(List.of(
                                                    selectedHashtag5,
                                                    selectedHashtag6
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
                            AppConfig.toUser(memberInit.get(selectedMember2))
                    );
                }
            }

            // 숙소 review

            for (Long i = 82L; i <= 102; i++) {

                List<String> randomMember3 = memberInit.keySet().stream()
                        .filter(memberId -> !memberId.equals("admin"))
                        .collect(Collectors.toList());

                for (int j = 0; j < 2; j++) {

                    double randomScore3 = 4.0 + (1.0) * random.nextDouble();
                    String[] randomTitle3 = {"멋있어요!", "너무 좋아요~!", "추천합니다!", "다시 오고 싶어요!", "분위기 좋네요!"};
                    String[] randomContent3 = {"추천해요~", "너무 멋있네요~!",
                            "분위기가 너무 좋아요!!", "주변 인프라가 좋아서 편해요!", "몇 번이고 추천합니다!"};
                    String[] randomHashtag7 = {"숙소", "여행 숙소", "데이트 코스"};
                    String[] randomHashtag8 = {"휴식", "나홀로", "둘이서"};

                    String formattedScore3 = String.format("%.1f", randomScore3);
                    double score3 = Double.parseDouble(formattedScore3);

                    String selectedMember3 = randomMember3.get(random.nextInt(randomMember3.size()));
                    String selectedTitle3 = randomTitle3[random.nextInt(randomTitle3.length)];
                    String selectedContent3 = randomContent3[random.nextInt(randomContent3.length)];
                    String selectedHashtag7 = randomHashtag7[random.nextInt(randomHashtag7.length)];
                    String selectedHashtag8 = randomHashtag8[random.nextInt(randomHashtag8.length)];

                    Review review3 = this.reviewService.create(
                            ReviewRequest.CreateReview.builder()
                                    .spotId(i)
                                    .year(2024)
                                    .month(1)
                                    .day(1)
                                    .title(selectedTitle3)
                                    .content(selectedContent3)
                                    .score(score3)
                                    .hashtags(List.of(
                                                    selectedHashtag7,
                                                    selectedHashtag8
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
                            AppConfig.toUser(memberInit.get(selectedMember3))
                    );
                }
            }

//            // comment init data

            for (Long i = 1L; i <= 102; i++) {

                String[] randomContent4 = {"감사합니다!", "위치가 정확히 어떻게 되나요?",
                        "가격이 어떻게 되나요?", "가족과 함께 가봐도 좋을까요?", "저도 추천합니다!"};

                List<String> randomMember4 = memberInit.keySet().stream()
                        .filter(memberId -> !memberId.equals("admin"))
                        .collect(Collectors.toList());

                String selectedMember4 = randomMember4.get(random.nextInt(randomMember4.size()));
                String selectedContent4 = randomContent4[random.nextInt(randomContent4.length)];

                Comment comment = this.commentService.create(
                        CommentRequest.CreateComment.builder()
                                .content(selectedContent4)
                                .reviewId(i)
                                .build(),
                        AppConfig.getMockErrors(),
                        AppConfig.toUser(memberInit.get(selectedMember4))
                );

            }

            for (Long i = 1L; i <= 102; i++) {

                String[] randomContent5 = {"감사합니다!", "댓글 감사합니다!",
                        "너무 좋은데요?", "한 번 다녀오시길 추천해요!", "완전 추천합니다!"};

                List<String> randomMember5 = memberInit.keySet().stream()
                        .filter(memberId -> !memberId.equals("admin"))
                        .collect(Collectors.toList());

                String selectedMember5 = randomMember5.get(random.nextInt(randomMember5.size()));
                String selectedContent5 = randomContent5[random.nextInt(randomContent5.length)];

                Comment reply = this.commentService.create(
                        CommentRequest.CreateComment.builder()
                                .content(selectedContent5)
                                .reviewId(i)
                                .tagId(i)
                                .build(),
                        AppConfig.getMockErrors(),
                        AppConfig.toUser(memberInit.get(selectedMember5))
                );

            }
        };
    }
}
