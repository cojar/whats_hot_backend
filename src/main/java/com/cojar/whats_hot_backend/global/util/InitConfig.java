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
            Category category2 = this.categoryService.create("2차", 2, gourmet.getId());
            Category category3 = this.categoryService.create("3차-1", 3, category2.getId());
            Category category4 = this.categoryService.create("3차-2", 3, category2.getId());

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
            Category sashimi = this.categoryService.create("회", 3, japaneseFood.getId());
            Category cutletUdon = this.categoryService.create("돈가스/우동", 3, japaneseFood.getId());
            Category pizza = this.categoryService.create("피자", 3, westernFood.getId());
            Category chicken = this.categoryService.create("치킨", 3, westernFood.getId());
            Category grilledMeat = this.categoryService.create("고기/구이", 3, koreanFood.getId());
            Category chineseCuisine = this.categoryService.create("중화", 3, chineseFood.getId());
            Category italianCuisine = this.categoryService.create("이탈리안", 3, westernFood.getId());
            Category stewAndSoup = this.categoryService.create("찜/탕/찌개", 3, koreanFood.getId());
            Category lunchBox = this.categoryService.create("도시락", 3, koreanFood.getId());
            Category bakery = this.categoryService.create("제과/빵/커피", 3, dessert.getId());
            Category fastFood = this.categoryService.create("패스트푸드", 3, westernFood.getId());
            Category koreanSnack = this.categoryService.create("떡볶이/순대/튀김/어묵", 3, snackFood.getId());
            Category midnightSnack = this.categoryService.create("야식", 3, koreanFood.getId());
            Category asianCuisine = this.categoryService.create("아시안푸드", 3, asianFood.getId());
            Category noodle = this.categoryService.create("면", 3, koreanFood.getId());

            // 국내여행지 대분류
            Category destination = this.categoryService.create("여행지", 1, -1L);

            // 국내여행지 중분류
            Category seoul = this.categoryService.create("서울", 2, destination.getId());
            Category busan = this.categoryService.create("부산", 2, destination.getId());
            Category incheon = this.categoryService.create("인천", 2, destination.getId());
            Category daegu = this.categoryService.create("대구", 2, destination.getId());
            Category gwangju = this.categoryService.create("광주", 2, destination.getId());
            Category daejeon = this.categoryService.create("대전", 2, destination.getId());
            Category ulsan = this.categoryService.create("울산", 2, destination.getId());
            Category sejong = this.categoryService.create("세종", 2, destination.getId());
            Category gyeonggi = this.categoryService.create("경기도", 2, destination.getId());
            Category gangwon = this.categoryService.create("강원도", 2, destination.getId());
            Category chungbuk = this.categoryService.create("충청북도", 2, destination.getId());
            Category chungnam = this.categoryService.create("충청남도", 2, destination.getId());
            Category jeonbuk = this.categoryService.create("전라북도", 2, destination.getId());
            Category jeonnam = this.categoryService.create("전라남도", 2, destination.getId());
            Category gyeongbuk = this.categoryService.create("경상북도", 2, destination.getId());
            Category gyeongnam = this.categoryService.create("경상남도", 2, destination.getId());
            Category jeju = this.categoryService.create("제주", 2, destination.getId());

            // 서울 소분류
            Category seoulETC = this.categoryService.create("서울 소분류 전체", 3, seoul.getId());
            Category seoulPalaces = this.categoryService.create("서울 궁궐", 3, seoul.getId());
            Category seoulParks = this.categoryService.create("서울 공원", 3, seoul.getId());
            Category seoulMuseums = this.categoryService.create("서울 박물관", 3, seoul.getId());
            Category seoulMarkets = this.categoryService.create("서울 재래시장", 3, seoul.getId());
            Category seoulLandmarks = this.categoryService.create("서울 랜드마크", 3, seoul.getId());
            Category seoulMountain = this.categoryService.create("서울 산", 3, seoul.getId());
            Category seoulValley = this.categoryService.create("서울 계곡", 3, seoul.getId());
            Category seoulRuins = this.categoryService.create("서울 유적", 3, seoul.getId());
            Category seoulThemePark = this.categoryService.create("서울 테마파크", 3, seoul.getId());
            Category seoulActivity = this.categoryService.create("서울 액티비티", 3, seoul.getId());

            // 부산 소분류
            Category busanETC = this.categoryService.create("부산 소분류 전체", 3, busan.getId());
            Category busanPalaces = this.categoryService.create("부산 궁궐", 3, busan.getId());
            Category busanParks = this.categoryService.create("부산 공원", 3, busan.getId());
            Category busanMuseums = this.categoryService.create("부산 박물관", 3, busan.getId());
            Category busanMarkets = this.categoryService.create("부산 재래시장", 3, busan.getId());
            Category busanLandmarks = this.categoryService.create("부산 랜드마크", 3, busan.getId());
            Category busanMountain = this.categoryService.create("부산 산", 3, busan.getId());
            Category busanValley = this.categoryService.create("부산 계곡", 3, busan.getId());
            Category busanOcean = this.categoryService.create("부산 바다", 3, busan.getId());
            Category busanRuins = this.categoryService.create("부산 유적", 3, busan.getId());
            Category busanThemePark = this.categoryService.create("부산 테마파크", 3, busan.getId());
            Category busanActivity = this.categoryService.create("부산 액티비티", 3, busan.getId());

            // 인천 소분류
            Category incheonETC = this.categoryService.create("인천 소분류 전체", 3, incheon.getId());
            Category incheonPalaces = this.categoryService.create("인천 궁궐", 3, incheon.getId());
            Category incheonParks = this.categoryService.create("인천 공원", 3, incheon.getId());
            Category incheonMuseums = this.categoryService.create("인천 박물관", 3, incheon.getId());
            Category incheonMarkets = this.categoryService.create("인천 재래시장", 3, incheon.getId());
            Category incheonLandmarks = this.categoryService.create("인천 랜드마크", 3, incheon.getId());
            Category incheonMountain = this.categoryService.create("인천 산", 3, incheon.getId());
            Category incheonValley = this.categoryService.create("인천 계곡", 3, incheon.getId());
            Category incheonOcean = this.categoryService.create("인천 바다", 3, incheon.getId());
            Category incheonRuins = this.categoryService.create("인천 유적", 3, incheon.getId());
            Category incheonThemePark = this.categoryService.create("인천 테마파크", 3, incheon.getId());
            Category incheonActivity = this.categoryService.create("인천 액티비티", 3, incheon.getId());

            // 대구 소분류
            Category daeguETC = this.categoryService.create("대구 소분류 전체", 3, daegu.getId());
            Category daeguPalaces = this.categoryService.create("대구 궁궐", 3, daegu.getId());
            Category daeguParks = this.categoryService.create("대구 공원", 3, daegu.getId());
            Category daeguMuseums = this.categoryService.create("대구 박물관", 3, daegu.getId());
            Category daeguMarkets = this.categoryService.create("대구 재래시장", 3, daegu.getId());
            Category daeguLandmarks = this.categoryService.create("대구 랜드마크", 3, daegu.getId());
            Category daeguMountain = this.categoryService.create("대구 산", 3, daegu.getId());
            Category daeguValley = this.categoryService.create("대구 계곡", 3, daegu.getId());
            Category daeguRuins = this.categoryService.create("대구 유적", 3, daegu.getId());
            Category daeguThemePark = this.categoryService.create("대구 테마파크", 3, daegu.getId());
            Category daeguActivity = this.categoryService.create("대구 액티비티", 3, daegu.getId());

            // 광주 소분류
            Category gwangjuETC = this.categoryService.create("광주 소분류 전체", 3, gwangju.getId());
            Category gwangjuPalaces = this.categoryService.create("광주 궁궐", 3, gwangju.getId());
            Category gwangjuParks = this.categoryService.create("광주 공원", 3, gwangju.getId());
            Category gwangjuMuseums = this.categoryService.create("광주 박물관", 3, gwangju.getId());
            Category gwangjuMarkets = this.categoryService.create("광주 재래시장", 3, gwangju.getId());
            Category gwangjuLandmarks = this.categoryService.create("광주 랜드마크", 3, gwangju.getId());
            Category gwangjuMountain = this.categoryService.create("광주 산", 3, gwangju.getId());
            Category gwangjuValley = this.categoryService.create("광주 계곡", 3, gwangju.getId());
            Category gwangjuRuins = this.categoryService.create("광주 유적", 3, gwangju.getId());
            Category gwangjuThemePark = this.categoryService.create("광주 테마파크", 3, gwangju.getId());
            Category gwangjuActivity = this.categoryService.create("광주 액티비티", 3, gwangju.getId());

            // 대전 소분류
            Category daejeonETC = this.categoryService.create("대전 소분류 전체", 3, daejeon.getId());
            Category daejeonPalaces = this.categoryService.create("대전 궁궐", 3, daejeon.getId());
            Category daejeonParks = this.categoryService.create("대전 공원", 3, daejeon.getId());
            Category daejeonMuseums = this.categoryService.create("대전 박물관", 3, daejeon.getId());
            Category daejeonMarkets = this.categoryService.create("대전 재래시장", 3, daejeon.getId());
            Category daejeonLandmarks = this.categoryService.create("대전 랜드마크", 3, daejeon.getId());
            Category daejeonMountain = this.categoryService.create("대전 산", 3, daejeon.getId());
            Category daejeonValley = this.categoryService.create("대전 계곡", 3, daejeon.getId());
            Category daejeonRuins = this.categoryService.create("대전 유적", 3, daejeon.getId());
            Category daejeonThemePark = this.categoryService.create("대전 테마파크", 3, daejeon.getId());
            Category daejeonActivity = this.categoryService.create("대전 액티비티", 3, daejeon.getId());

            // 울산 소분류
            Category ulsanETC = this.categoryService.create("울산 소분류 전체", 3, ulsan.getId());
            Category ulsanPalaces = this.categoryService.create("울산 궁궐", 3, ulsan.getId());
            Category ulsanParks = this.categoryService.create("울산 공원", 3, ulsan.getId());
            Category ulsanMuseums = this.categoryService.create("울산 박물관", 3, ulsan.getId());
            Category ulsanMarkets = this.categoryService.create("울산 재래시장", 3, ulsan.getId());
            Category ulsanLandmarks = this.categoryService.create("울산 랜드마크", 3, ulsan.getId());
            Category ulsanMountain = this.categoryService.create("울산 산", 3, ulsan.getId());
            Category ulsanValley = this.categoryService.create("울산 계곡", 3, ulsan.getId());
            Category ulsanOcean = this.categoryService.create("울산 바다", 3, ulsan.getId());
            Category ulsanRuins = this.categoryService.create("울산 유적", 3, ulsan.getId());
            Category ulsanThemePark = this.categoryService.create("울산 테마파크", 3, ulsan.getId());
            Category ulsanActivity = this.categoryService.create("울산 액티비티", 3, ulsan.getId());

            // 세종 소분류
            Category sejongETC = this.categoryService.create("세종 소분류 전체", 3, sejong.getId());
            Category sejongPalaces = this.categoryService.create("세종 궁궐", 3, sejong.getId());
            Category sejongParks = this.categoryService.create("세종 공원", 3, sejong.getId());
            Category sejongMuseums = this.categoryService.create("세종 박물관", 3, sejong.getId());
            Category sejongMarkets = this.categoryService.create("세종 재래시장", 3, sejong.getId());
            Category sejongLandmarks = this.categoryService.create("세종 랜드마크", 3, sejong.getId());
            Category sejongMountain = this.categoryService.create("세종 산", 3, sejong.getId());
            Category sejongValley = this.categoryService.create("세종 계곡", 3, sejong.getId());
            Category sejongRuins = this.categoryService.create("세종 유적", 3, sejong.getId());
            Category sejongThemePark = this.categoryService.create("세종 테마파크", 3, sejong.getId());
            Category sejongActivity = this.categoryService.create("세종 액티비티", 3, sejong.getId());

            // 경기 소분류
            Category gyeonggiETC = this.categoryService.create("경기 소분류 전체", 3, gyeonggi.getId());
            Category gyeonggiPalaces = this.categoryService.create("경기 궁궐", 3, gyeonggi.getId());
            Category gyeonggiParks = this.categoryService.create("경기 공원", 3, gyeonggi.getId());
            Category gyeonggiMuseums = this.categoryService.create("경기 박물관", 3, gyeonggi.getId());
            Category gyeonggiMarkets = this.categoryService.create("경기 재래시장", 3, gyeonggi.getId());
            Category gyeonggiLandmarks = this.categoryService.create("경기 랜드마크", 3, gyeonggi.getId());
            Category gyeonggiMountain = this.categoryService.create("경기 산", 3, gyeonggi.getId());
            Category gyeonggiValley = this.categoryService.create("경기 계곡", 3, gyeonggi.getId());
            Category gyeonggiOcean = this.categoryService.create("경기 바다", 3, gyeonggi.getId());
            Category gyeonggiRuins = this.categoryService.create("경기 유적", 3, gyeonggi.getId());
            Category gyeonggiThemePark = this.categoryService.create("경기 테마파크", 3, gyeonggi.getId());
            Category gyeonggiActivity = this.categoryService.create("경기 액티비티", 3, gyeonggi.getId());

            // 강원 소분류
            Category gangwonETC = this.categoryService.create("강원 소분류 전체", 3, gangwon.getId());
            Category gangwonPalaces = this.categoryService.create("강원 궁궐", 3, gangwon.getId());
            Category gangwonParks = this.categoryService.create("강원 공원", 3, gangwon.getId());
            Category gangwonMuseums = this.categoryService.create("강원 박물관", 3, gangwon.getId());
            Category gangwonMarkets = this.categoryService.create("강원 재래시장", 3, gangwon.getId());
            Category gangwonLandmarks = this.categoryService.create("강원 랜드마크", 3, gangwon.getId());
            Category gangwonMountain = this.categoryService.create("강원 산", 3, gangwon.getId());
            Category gangwonValley = this.categoryService.create("강원 계곡", 3, gangwon.getId());
            Category gangwonOcean = this.categoryService.create("강원 바다", 3, gangwon.getId());
            Category gangwonRuins = this.categoryService.create("강원 유적", 3, gangwon.getId());
            Category gangwonThemePark = this.categoryService.create("강원 테마파크", 3, gangwon.getId());
            Category gangwonActivity = this.categoryService.create("강원 액티비티", 3, gangwon.getId());

            // 충청북도 소분류
            Category chungbukETC = this.categoryService.create("충북 소분류 전체", 3, chungbuk.getId());
            Category chungbukPalaces = this.categoryService.create("충북 궁궐", 3, chungbuk.getId());
            Category chungbukParks = this.categoryService.create("충북 공원", 3, chungbuk.getId());
            Category chungbukMuseums = this.categoryService.create("충북 박물관", 3, chungbuk.getId());
            Category chungbukMarkets = this.categoryService.create("충북 재래시장", 3, chungbuk.getId());
            Category chungbukLandmarks = this.categoryService.create("충북 랜드마크", 3, chungbuk.getId());
            Category chungbukMountain = this.categoryService.create("충북 산", 3, chungbuk.getId());
            Category chungbukValley = this.categoryService.create("충북 계곡", 3, chungbuk.getId());
            Category chungbukRuins = this.categoryService.create("충북 유적", 3, chungbuk.getId());
            Category chungbukThemePark = this.categoryService.create("충북 테마파크", 3, chungbuk.getId());
            Category chungbukActivity = this.categoryService.create("충북 액티비티", 3, chungbuk.getId());

            // 충청남도 소분류
            Category chungnamETC = this.categoryService.create("충남 소분류 전체", 3, chungnam.getId());
            Category chungnamPalaces = this.categoryService.create("충남 궁궐", 3, chungnam.getId());
            Category chungnamParks = this.categoryService.create("충남 공원", 3, chungnam.getId());
            Category chungnamMuseums = this.categoryService.create("충남 박물관", 3, chungnam.getId());
            Category chungnamMarkets = this.categoryService.create("충남 재래시장", 3, chungnam.getId());
            Category chungnamLandmarks = this.categoryService.create("충남 랜드마크", 3, chungnam.getId());
            Category chungnamMountain = this.categoryService.create("충남 산", 3, chungnam.getId());
            Category chungnamValley = this.categoryService.create("충남 계곡", 3, chungnam.getId());
            Category chungnamOcean = this.categoryService.create("충남 바다", 3, chungnam.getId());
            Category chungnamRuins = this.categoryService.create("충남 유적", 3, chungnam.getId());
            Category chungnamThemePark = this.categoryService.create("충남 테마파크", 3, chungnam.getId());
            Category chungnamActivity = this.categoryService.create("충남 액티비티", 3, chungnam.getId());

            // 전라북도 소분류
            Category jeonbukETC = this.categoryService.create("전북 소분류 전체", 3, jeonbuk.getId());
            Category jeonbukPalaces = this.categoryService.create("전북 궁궐", 3, jeonbuk.getId());
            Category jeonbukParks = this.categoryService.create("전북 공원", 3, jeonbuk.getId());
            Category jeonbukMuseums = this.categoryService.create("전북 박물관", 3, jeonbuk.getId());
            Category jeonbukMarkets = this.categoryService.create("전북 재래시장", 3, jeonbuk.getId());
            Category jeonbukLandmarks = this.categoryService.create("전북 랜드마크", 3, jeonbuk.getId());
            Category jeonbukMountain = this.categoryService.create("전북 산", 3, jeonbuk.getId());
            Category jeonbukValley = this.categoryService.create("전북 계곡", 3, jeonbuk.getId());
            Category jeonbukOcean = this.categoryService.create("전북 바다", 3, jeonbuk.getId());
            Category jeonbukRuins = this.categoryService.create("전북 유적", 3, jeonbuk.getId());
            Category jeonbukThemePark = this.categoryService.create("전북 테마파크", 3, jeonbuk.getId());
            Category jeonbukActivity = this.categoryService.create("전북 액티비티", 3, jeonbuk.getId());

            // 전라남도 소분류
            Category jeonnamETC = this.categoryService.create("전남 소분류 전체", 3, jeonnam.getId());
            Category jeonnamPalaces = this.categoryService.create("전남 궁궐", 3, jeonnam.getId());
            Category jeonnamParks = this.categoryService.create("전남 공원", 3, jeonnam.getId());
            Category jeonnamMuseums = this.categoryService.create("전남 박물관", 3, jeonnam.getId());
            Category jeonnamMarkets = this.categoryService.create("전남 재래시장", 3, jeonnam.getId());
            Category jeonnamLandmarks = this.categoryService.create("전남 랜드마크", 3, jeonnam.getId());
            Category jeonnamMountain = this.categoryService.create("전남 산", 3, jeonnam.getId());
            Category jeonnamValley = this.categoryService.create("전남 계곡", 3, jeonnam.getId());
            Category jeonnamOcean = this.categoryService.create("전남 바다", 3, jeonnam.getId());
            Category jeonnamRuins = this.categoryService.create("전남 유적", 3, jeonnam.getId());
            Category jeonnamThemePark = this.categoryService.create("전남 테마파크", 3, jeonnam.getId());
            Category jeonnamActivity = this.categoryService.create("전남 액티비티", 3, jeonnam.getId());

            // 경상북도 소분류
            Category gyeongbukETC = this.categoryService.create("경북 소분류 전체", 3, gyeongbuk.getId());
            Category gyeongbukPalaces = this.categoryService.create("경북 궁궐", 3, gyeongbuk.getId());
            Category gyeongbukParks = this.categoryService.create("경북 공원", 3, gyeongbuk.getId());
            Category gyeongbukMuseums = this.categoryService.create("경북 박물관", 3, gyeongbuk.getId());
            Category gyeongbukMarkets = this.categoryService.create("경북 재래시장", 3, gyeongbuk.getId());
            Category gyeongbukLandmarks = this.categoryService.create("경북 랜드마크", 3, gyeongbuk.getId());
            Category gyeongbukMountain = this.categoryService.create("경북 산", 3, gyeongbuk.getId());
            Category gyeongbukValley = this.categoryService.create("경북 계곡", 3, gyeongbuk.getId());
            Category gyeongbukOcean = this.categoryService.create("경북 바다", 3, gyeongbuk.getId());
            Category gyeongbukRuins = this.categoryService.create("경북 유적", 3, gyeongbuk.getId());
            Category gyeongbukThemePark = this.categoryService.create("경북 테마파크", 3, gyeongbuk.getId());
            Category gyeongbukActivity = this.categoryService.create("경북 액티비티", 3, gyeongbuk.getId());

            // 경상남도 소분류
            Category gyeongnamETC = this.categoryService.create("경남 소분류 전체", 3, gyeongnam.getId());
            Category gyeongnamPalaces = this.categoryService.create("경남 궁궐", 3, gyeongnam.getId());
            Category gyeongnamParks = this.categoryService.create("경남 공원", 3, gyeongnam.getId());
            Category gyeongnamMuseums = this.categoryService.create("경남 박물관", 3, gyeongnam.getId());
            Category gyeongnamMarkets = this.categoryService.create("경남 재래시장", 3, gyeongnam.getId());
            Category gyeongnamLandmarks = this.categoryService.create("경남 랜드마크", 3, gyeongnam.getId());
            Category gyeongnamMountain = this.categoryService.create("경남 산", 3, gyeongnam.getId());
            Category gyeongnamValley = this.categoryService.create("경남 계곡", 3, gyeongnam.getId());
            Category gyeongnamOcean = this.categoryService.create("경남 바다", 3, gyeongnam.getId());
            Category gyeongnamRuins = this.categoryService.create("경남 유적", 3, gyeongnam.getId());
            Category gyeongnamThemePark = this.categoryService.create("경남 테마파크", 3, gyeongnam.getId());
            Category gyeongnamActivity = this.categoryService.create("경남 액티비티", 3, gyeongnam.getId());

            // 경상남도 소분류
            Category jejuETC = this.categoryService.create("제주 소분류 전체", 3, jeju.getId());
            Category jejuPalaces = this.categoryService.create("제주 궁궐", 3, jeju.getId());
            Category jejuParks = this.categoryService.create("제주 공원", 3, jeju.getId());
            Category jejuMuseums = this.categoryService.create("제주 박물관", 3, jeju.getId());
            Category jejuMarkets = this.categoryService.create("제주 재래시장", 3, jeju.getId());
            Category jejuLandmarks = this.categoryService.create("제주 랜드마크", 3, jeju.getId());
            Category jejuMountain = this.categoryService.create("제주 산", 3, jeju.getId());
            Category jejuValley = this.categoryService.create("제주 계곡", 3, jeju.getId());
            Category jejuOcean = this.categoryService.create("제주 바다", 3, jeju.getId());
            Category jejuRuins = this.categoryService.create("제주 유적", 3, jeju.getId());
            Category jejuThemePark = this.categoryService.create("제주 테마파크", 3, jeju.getId());
            Category jejuActivity = this.categoryService.create("제주 액티비티", 3, jeju.getId());

            // 숙박 대분류
            Category lodging = this.categoryService.create("숙박", 1, -1L);

            // 숙박 중분류
            Category lodgingSub = this.categoryService.create("숙박 중분류 전체", 2, lodging.getId());
            Category hotelAndResort = this.categoryService.create("호텔/리조트", 2, lodging.getId());
            Category Pension = this.categoryService.create("펜션", 2, lodging.getId());
            Category poolVilla = this.categoryService.create("풀빌라", 2, lodging.getId());
            Category familyStyle = this.categoryService.create("가족형숙소", 2, lodging.getId());
            Category motel = this.categoryService.create("모텔", 2, lodging.getId());
            Category camping = this.categoryService.create("캠핑", 2, lodging.getId());
            Category guesthouse = this.categoryService.create("게스트하우스", 2, lodging.getId());

            // 숙박 소분류
            Category lodgingDetail = this.categoryService.create("숙박 소분류 전체", 3, lodging.getId());
            Category hotelAndResortDetail = this.categoryService.create("호텔/리조트", 3, lodging.getId());
            Category PensionDetail = this.categoryService.create("펜션", 3, lodging.getId());
            Category poolVillaDetail = this.categoryService.create("풀빌라", 3, lodging.getId());
            Category familyStyleDetail = this.categoryService.create("가족형숙소", 3, lodging.getId());
            Category motelDetail = this.categoryService.create("모텔", 3, lodging.getId());
            Category campingDetail = this.categoryService.create("캠핑", 3, lodging.getId());
            Category guesthouseDetial = this.categoryService.create("게스트하우스", 3, lodging.getId());

            // spot init data
            Spot spot1 = this.spotService.create(
                    SpotRequest.CreateSpot.builder()
                            .categoryId(category3.getId())
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
                            .categoryId(category3.getId())
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
