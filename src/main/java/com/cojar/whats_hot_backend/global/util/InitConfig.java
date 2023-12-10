package com.cojar.whats_hot_backend.global.util;

import com.cojar.whats_hot_backend.domain.category.entity.Category;
import com.cojar.whats_hot_backend.domain.category.service.CategoryService;
import com.cojar.whats_hot_backend.domain.file.entity.SaveFile;
import com.cojar.whats_hot_backend.domain.file.service.FileService;
import com.cojar.whats_hot_backend.domain.hashtag.entity.Hashtag;
import com.cojar.whats_hot_backend.domain.hashtag.service.HashtagService;
import com.cojar.whats_hot_backend.domain.member.entity.Member;
import com.cojar.whats_hot_backend.domain.member.entity.MemberRole;
import com.cojar.whats_hot_backend.domain.member.service.MemberService;
import com.cojar.whats_hot_backend.domain.menu_item.entity.MenuItem;
import com.cojar.whats_hot_backend.domain.menu_item.service.MenuItemService;
import com.cojar.whats_hot_backend.domain.spot.entity.Spot;
import com.cojar.whats_hot_backend.domain.spot.service.SpotService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

            MenuItem menuItem1 = this.menuItemService.create("메뉴1", "10000원", spot1);
            MenuItem menuItem2 = this.menuItemService.create("메뉴2", "20000원", spot1);
            MenuItem menuItem3 = this.menuItemService.create("메뉴3", "30000원", spot1);

            SaveFile image1 = this.fileService.create(spot1);
            SaveFile image2 = this.fileService.create(spot1);
            SaveFile image3 = this.fileService.create(spot1);
        };
    }
}
