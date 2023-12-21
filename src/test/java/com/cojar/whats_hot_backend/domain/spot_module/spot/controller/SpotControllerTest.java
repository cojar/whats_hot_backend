package com.cojar.whats_hot_backend.domain.spot_module.spot.controller;

import com.cojar.whats_hot_backend.domain.spot_module.category.entity.Category;
import com.cojar.whats_hot_backend.domain.spot_module.category.service.CategoryService;
import com.cojar.whats_hot_backend.domain.spot_module.menu_item.dto.MenuItemDto;
import com.cojar.whats_hot_backend.domain.spot_module.spot.request.SpotRequest;
import com.cojar.whats_hot_backend.global.controller.BaseControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.ResultActions;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class SpotControllerTest extends BaseControllerTest {

    @Autowired
    private CategoryService categoryService;

    @Test
    @DisplayName("post:/api/spots - created, S-02-01")
    public void createSpot_Created() throws Exception {

        // given
        String username = "admin";
        String password = "1234";
        String accessToken = "Bearer " + this.memberService.getAccessToken(loginReq.of(username, password));

        Category category = this.categoryService.getCategoryById(3L);
        String name = "쿠우쿠우 대전둔산점";
        String address = "대전 서구 대덕대로233번길 17 해운빌딩 4층";
        String contact = "042-489-6274";
        String hashtag1 = "뷔페", hashtag2 = "초밥";
        String menuName1 = "평일점심", menuPrice1 = "20,900원";
        String menuName2 = "평일저녁", menuPrice2 = "24,900원";
        String menuName3 = "주말/공휴일", menuPrice3 = "26,900원";
        SpotRequest.CreateSpot request = SpotRequest.CreateSpot.builder()
                .categoryId(category.getId())
                .name(name)
                .address(address)
                .contact(contact)
                .hashtags(List.of(hashtag1, hashtag2))
                .menuItems(List.of(
                        MenuItemDto.of(menuName1, menuPrice1),
                        MenuItemDto.of(menuName2, menuPrice2),
                        MenuItemDto.of(menuName3, menuPrice3)
                ))
                .build();
        MockMultipartFile _request = new MockMultipartFile(
                "request",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                this.objectMapper.writeValueAsString(request).getBytes(StandardCharsets.UTF_8)
        );

        String fileName = "test";
        String ext = "png";
        Resource resource = resourceLoader.getResource("classpath:/static/image/%s.%s".formatted(fileName, ext));
        MockMultipartFile _file1 = new MockMultipartFile(
                "images",
                "%s.%s".formatted(fileName, ext),
                MediaType.IMAGE_PNG_VALUE,
                resource.getInputStream()
        );
        MockMultipartFile _file2 = new MockMultipartFile(
                "images",
                "%s.%s".formatted(fileName, ext),
                MediaType.IMAGE_PNG_VALUE,
                resource.getInputStream()
        );

        // when
        ResultActions resultActions = this.mockMvc
                .perform(multipart(HttpMethod.POST, "/api/spots")
                        .file(_request)
                        .file(_file1)
                        .file(_file2)
                        .header("Authorization", accessToken)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaTypes.HAL_JSON)
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("success").value("true"))
                .andExpect(jsonPath("code").value("S-02-01"))
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("data.id").exists())
                .andExpect(jsonPath("data.createDate").exists())
                .andExpect(jsonPath("data.modifyDate").exists())
                .andExpect(jsonPath("data.category").value(category.toLine()))
                .andExpect(jsonPath("data.name").value(name))
                .andExpect(jsonPath("data.address").value(address))
                .andExpect(jsonPath("data.contact").value(contact))
                .andExpect(jsonPath("data.averageScore").value(0.0))
                .andExpect(jsonPath("data.hashtags[0]").value(hashtag1))
                .andExpect(jsonPath("data.hashtags[1]").value(hashtag2))
                .andExpect(jsonPath("data.menuItems[0].name").value(menuName1))
                .andExpect(jsonPath("data.menuItems[0].price").value(menuPrice1))
                .andExpect(jsonPath("data.menuItems[1].name").value(menuName2))
                .andExpect(jsonPath("data.menuItems[1].price").value(menuPrice2))
                .andExpect(jsonPath("data.menuItems[2].name").value(menuName3))
                .andExpect(jsonPath("data.menuItems[2].price").value(menuPrice3))
                .andExpect(jsonPath("data.imageUri[0]").exists())
                .andExpect(jsonPath("data.imageUri[1]").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
        ;


    }

}