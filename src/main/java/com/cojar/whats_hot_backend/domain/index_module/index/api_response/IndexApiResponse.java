package com.cojar.whats_hot_backend.domain.index_module.index.api_response;

import com.cojar.whats_hot_backend.global.response.ResData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.hateoas.MediaTypes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public @interface IndexApiResponse {

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "API 인덱스",
            description = "사용가능한 API 링크들을 반환한다",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "정상 응답",
                            content = @Content(mediaType = MediaTypes.HAL_JSON_VALUE,
                                    schema = @Schema(implementation = ResData.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                "status": "OK",
                                                "success": true,
                                                "code": "S-00-00",
                                                "message": "인덱스 링크 목록을 반환합니다",
                                                "_links": {
                                                "self": {
                                                    "href": "http://localhost:8080/api/index"
                                                },
                                                "profile": {
                                                    "href": "http://localhost:8080/swagger-ui/index.html"
                                                }
                                             }
                                            }
                                            """
                                    )
                            )
                    )
            }
    )
    public @interface Index {
    }
}