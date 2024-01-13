package com.cojar.whats_hot_backend.domain.base_module.file.service;

import com.cojar.whats_hot_backend.domain.base_module.file.entity.FileDomain;
import com.cojar.whats_hot_backend.domain.base_module.file.entity._File;
import com.cojar.whats_hot_backend.global.errors.exception.ApiResponseException;
import com.cojar.whats_hot_backend.global.response.ResData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
class FileServiceTest {

    @Value("${file.origin.path}")
    private String origin;

    @Autowired
    private FileService fileService;

    @Autowired
    private ResourceLoader resourceLoader;

    @ParameterizedTest
    @MethodSource("argsFor_validate_Success")
    @DisplayName("validate success")
    public void validate_Success(String name, String ext) throws Exception {

        // given
        Resource resource = resourceLoader.getResource("classpath:/static/image/%s.%s".formatted(name, ext));
        MockMultipartFile _file = new MockMultipartFile(
                "file",
                "%s.%s".formatted(name, ext),
                "image/%s".formatted(ext),
                resource.getInputStream()
        );

        // when, then
        assertDoesNotThrow(() -> this.fileService.validateUnit(_file));
    }

    private static Stream<Arguments> argsFor_validate_Success() {
        return Stream.of(
                Arguments.of("test", "jpg"),
                Arguments.of("test", "jpeg"),
                Arguments.of("test", "png")
        );
    }

    @Test
    @DisplayName("validate fail; content type, F-00-00-01")
    public void validate_Fail_ContentType() throws Exception {

        // given
        String name = "test";
        String ext = "png";
        Resource resource = resourceLoader.getResource("classpath:/static/image/%s.%s".formatted(name, ext));
        MockMultipartFile _file = new MockMultipartFile(
                "file",
                "%s.%s".formatted(name, ext),
                "aaa/%s".formatted(ext),
                resource.getInputStream()
        );

        // when
        ResData resData = assertThrows(ApiResponseException.class, () -> this.fileService.validateUnit(_file)).getResData();

        // then
        assertThat(resData).isNotNull();
        assertThat(resData.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(resData.isSuccess()).isFalse();
        assertThat(resData.getCode()).isEqualTo("F-00-00-01");
        assertThat(resData.getMessage()).isEqualTo("이미지 형식만 업로드할 수 있습니다");
        assertThat(resData.getData()).isInstanceOf(Errors.class);

        Errors errors = (Errors) resData.getData();
        FieldError fieldError = errors.getFieldErrors().get(0);

        assertThat(fieldError.getField()).isEqualTo("contentType");
        assertThat(fieldError.getObjectName()).isEqualTo("file");
        assertThat(fieldError.getCode()).isEqualTo("not allowed");
        assertThat(fieldError.getDefaultMessage()).isEqualTo("content type is not allowed");
        assertThat(fieldError.getRejectedValue()).isEqualTo("aaa/%s".formatted(ext));
    }

    @Test
    @DisplayName("validate fail; extension, F-00-00-02")
    public void validate_Fail_Extension() throws Exception {

        // given
        String name = "test";
        String ext = "a";
        Resource resource = resourceLoader.getResource("classpath:/static/image/%s.%s".formatted(name, ext));
        MockMultipartFile _file = new MockMultipartFile(
                "file",
                "%s.%s".formatted(name, ext),
                "image/%s".formatted(ext),
                resource.getInputStream()
        );

        // when
        ResData resData = assertThrows(ApiResponseException.class, () -> this.fileService.validateUnit(_file)).getResData();

        // then
        assertThat(resData).isNotNull();
        assertThat(resData.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(resData.isSuccess()).isFalse();
        assertThat(resData.getCode()).isEqualTo("F-00-00-02");
        assertThat(resData.getMessage()).isEqualTo("JPG, JPEG, PNG 확장자만 업로드할 수 있습니다");
        assertThat(resData.getData()).isInstanceOf(Errors.class);

        Errors errors = (Errors) resData.getData();
        FieldError fieldError = errors.getFieldErrors().get(0);

        assertThat(fieldError.getField()).isEqualTo("contentType");
        assertThat(fieldError.getObjectName()).isEqualTo("file");
        assertThat(fieldError.getCode()).isEqualTo("not allowed");
        assertThat(fieldError.getDefaultMessage()).isEqualTo("file extension is not allowed");
        assertThat(fieldError.getRejectedValue()).isEqualTo("image/%s".formatted(ext));
    }

    @ParameterizedTest
    @MethodSource("argsFor_create_Success")
    @DisplayName("create success")
    public void create_Success(String name, String ext) throws Exception {

        // given
        Resource resource = resourceLoader.getResource("classpath:/static/image/%s.%s".formatted(name, ext));
        MockMultipartFile _file = new MockMultipartFile(
                "file",
                "%s.%s".formatted(name, ext),
                "image/%s".formatted(ext),
                resource.getInputStream()
        );

        // when
        _File file = this.fileService.createUnit(_file, FileDomain.MEMBER);

        // then
        assertThat(file).isNotNull();
        assertThat(file.getDomain()).isEqualTo(FileDomain.MEMBER);
        assertThat(file.getUuid()).isNotEmpty();
        assertThat(file.getName()).isNotEmpty();
        assertThat(file.getSize()).isNotNull();
        assertThat(file.getExt()).isNotEmpty();
    }

    private static Stream<Arguments> argsFor_create_Success() {
        return Stream.of(
                Arguments.of("test", "jpg"),
                Arguments.of("test", "jpeg"),
                Arguments.of("test", "png")
        );
    }
}