package com.cojar.whats_hot_backend.domain.spot_module.category.controller;

import com.cojar.whats_hot_backend.domain.base_module.file.service.FileService;
import com.cojar.whats_hot_backend.domain.spot_module.category.entity.Category;
import com.cojar.whats_hot_backend.domain.spot_module.category.request.CategoryRequest;
import com.cojar.whats_hot_backend.domain.spot_module.category.service.CategoryService;
import com.cojar.whats_hot_backend.domain.spot_module.menu_item.service.MenuItemService;
import com.cojar.whats_hot_backend.domain.spot_module.spot.service.SpotService;
import com.cojar.whats_hot_backend.domain.spot_module.spot_hashtag.service.SpotHashtagService;
import com.cojar.whats_hot_backend.domain.spot_module.spot_image.service.SpotImageService;
import com.cojar.whats_hot_backend.global.controller.BaseControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CategoryControllerTest extends BaseControllerTest {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SpotService spotService;

    @Autowired
    private SpotHashtagService spotHashtagService;

    @Autowired
    private MenuItemService menuItemService;

    @Autowired
    private SpotImageService spotImageService;

    @Autowired
    private FileService fileService;

    // 생성 성공
    @Transactional
    @ParameterizedTest
    @MethodSource("argsFor_createCategory_Created")
    @DisplayName("post:/api/categories - created, S-05-01")
    public void createCategory_Created(String name, Long parentId, Boolean allowRoot) throws Exception {

        // given
        String username = "admin";
        String password = "1234";
        String accessToken = this.getAccessToken(username, password);

        CategoryRequest.CreateCategory request = CategoryRequest.CreateCategory.builder()
                .name(name)
                .parentId(parentId)
                .allowRoot(allowRoot)
                .build();

        // when
        ResultActions resultActions = this.mockMvc
                .perform(post("/api/categories")
                        .header("Authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(request))
                        .accept(MediaTypes.HAL_JSON)
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("status").value("CREATED"))
                .andExpect(jsonPath("success").value("true"))
                .andExpect(jsonPath("code").value("S-05-01"))
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("data.id").exists())
                .andExpect(jsonPath("data.createDate").exists())
                .andExpect(jsonPath("data.modifyDate").exists())
                .andExpect(jsonPath("data.name").value(name))
        ;
        if (parentId != null) resultActions.andExpect(jsonPath("data.parentId").value(parentId));
    }

    private static Stream<Arguments> argsFor_createCategory_Created() {
        return Stream.of(
                // Root 카테고리 등록
                Arguments.of("항공권", null, true),
                // 하위 카테고리 등록
                Arguments.of("퓨전요리", 1L, false),
                Arguments.of("카라반", 1L, false)
        );
    }


    // 실패 1 유효성 검사
    @Transactional
    @ParameterizedTest
    @MethodSource("argsFor_createCategory_BadRequest_NotBlank")
    @DisplayName("post:/api/categories - created, F-05-01-01")
    public void createCategory_BadRequest_NotBlank(String name, Long parentId, Boolean allowRoot) throws Exception {

        // given
        String username = "admin";
        String password = "1234";
        String accessToken = this.getAccessToken(username, password);

        CategoryRequest.CreateCategory request = CategoryRequest.CreateCategory.builder()
                .name(name)
                .parentId(parentId)
                .allowRoot(allowRoot)
                .build();

        // when
        ResultActions resultActions = this.mockMvc
                .perform(post("/api/categories")
                        .header("Authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(request))
                        .accept(MediaTypes.HAL_JSON)
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value("BAD_REQUEST"))
                .andExpect(jsonPath("success").value("false"))
                .andExpect(jsonPath("code").value("F-05-01-01"))
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("data").doesNotExist());
    }

    private static Stream<Arguments> argsFor_createCategory_BadRequest_NotBlank() {
        return Stream.of(
                Arguments.of("", 1L,false)    // 이름값이 빈 문자열일 때
        );
    }

    // 실패 2, 허용되지 않은 1차 카테고리 등록
    @Transactional
    @ParameterizedTest
    @MethodSource("argsFor_createCategory_BadRequest_NotAllowRoot")
    @DisplayName("post:/api/categories - created, F-05-01-02")
    public void createCategory_BadRequest_NotAllowRoot(String name, Long parentId, Boolean allowRoot) throws Exception {

        // given
        String username = "admin";
        String password = "1234";
        String accessToken = this.getAccessToken(username, password);

        CategoryRequest.CreateCategory request = CategoryRequest.CreateCategory.builder()
                .name(name)
                .parentId(parentId)
                .allowRoot(allowRoot)
                .build();

        // when
        ResultActions resultActions = this.mockMvc
                .perform(post("/api/categories")
                        .header("Authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(request))
                        .accept(MediaTypes.HAL_JSON)
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value("BAD_REQUEST"))
                .andExpect(jsonPath("success").value("false"))
                .andExpect(jsonPath("code").value("F-05-01-02"))
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("data").doesNotExist());
    }

    private static Stream<Arguments> argsFor_createCategory_BadRequest_NotAllowRoot() {
        return Stream.of(
                Arguments.of("짬뽕", null,false)    // 허용되지 않은 1차 카테고리 등록일때
        );
    }

    // 실패 3, 하위 카테고리 등록시 상위 카테고리가 없음
    @Transactional
    @ParameterizedTest
    @MethodSource("argsFor_createCategory_BadRequest_NoParent")
    @DisplayName("post:/api/categories - created, F-05-01-03")
    public void createCategory_BadRequest_NoParent(String name, Long parentId, Boolean allowRoot) throws Exception {

        // given
        String username = "admin";
        String password = "1234";
        String accessToken = this.getAccessToken(username, password);

        CategoryRequest.CreateCategory request = CategoryRequest.CreateCategory.builder()
                .name(name)
                .parentId(parentId)
                .allowRoot(allowRoot)
                .build();

        // when
        ResultActions resultActions = this.mockMvc
                .perform(post("/api/categories")
                        .header("Authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(request))
                        .accept(MediaTypes.HAL_JSON)
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value("BAD_REQUEST"))
                .andExpect(jsonPath("success").value("false"))
                .andExpect(jsonPath("code").value("F-05-01-03"))
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("data").doesNotExist());
    }

    private static Stream<Arguments> argsFor_createCategory_BadRequest_NoParent() {
        return Stream.of(
                Arguments.of("짬뽕", 2000L,false)    // 2번째 parameter 인 parentId가 해당 Id를 가진 parent가 없을 때
        );
    }

    // 실패 4, 카테고리 차수가 맛집인 경우 3차까지, 나머지는 2차까지 일때 그 외의 값
    @Transactional
    @ParameterizedTest
    @MethodSource("argsFor_createCategory_BadRequest_OverDepth")
    @DisplayName("post:/api/categories - created, F-05-01-04")
    public void createCategory_BadRequest_OverDepth(String name, Long parentId, Boolean allowRoot) throws Exception {

        // given
        String username = "admin";
        String password = "1234";
        String accessToken = this.getAccessToken(username, password);

        CategoryRequest.CreateCategory request = CategoryRequest.CreateCategory.builder()
                .name(name)
                .parentId(parentId)
                .allowRoot(allowRoot)
                .build();

        // when
        ResultActions resultActions = this.mockMvc
                .perform(post("/api/categories")
                        .header("Authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(request))
                        .accept(MediaTypes.HAL_JSON)
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value("BAD_REQUEST"))
                .andExpect(jsonPath("success").value("false"))
                .andExpect(jsonPath("code").value("F-05-01-04"))
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("data").doesNotExist());
    }

    private static Stream<Arguments> argsFor_createCategory_BadRequest_OverDepth() {
        return Stream.of(
                Arguments.of("짬뽕", 24L, false),    // 맛집 4차 카테고리 등록시
                Arguments.of("서울", 36L, false),    // 여행지 3차 카테고리 등록시
                Arguments.of("PC방", 44L, false)   // 숙박 3차 카테고리 등록시
        );
    }




}