package com.cojar.whats_hot_backend.domain.spot_module.category.api_response;

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

public @interface CategoryApiResponse {

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "장소 카테고리 목록 조회",
            description = "성공 시 요청한 장소 카테고리 목록을 반환한다",
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
                                                "code": "S-05-02",
                                                "message": "요청하신 장소 카테고리 목록을 반환합니다",
                                                "data": {
                                                    "list": [
                                                        {
                                                            "id": 1,
                                                            "name": "맛집",
                                                            "_links": {
                                                                "self": {
                                                                    "href": "http://localhost:8080/api/categories/1"
                                                                }
                                                            }
                                                        },
                                                        {
                                                            "id": 25,
                                                            "name": "여행지",
                                                            "_links": {
                                                                "self": {
                                                                    "href": "http://localhost:8080/api/categories/25"
                                                                }
                                                            }
                                                        },
                                                        {
                                                            "id": 37,
                                                            "name": "숙박",
                                                            "_links": {
                                                                "self": {
                                                                    "href": "http://localhost:8080/api/categories/37"
                                                                }
                                                            }
                                                        }
                                                    ]
                                                },
                                                "_links": {
                                                    "self": {
                                                        "href": "http://localhost:8080/api/categories?parentId=-1"
                                                    },
                                                    "profile": {
                                                        "href": "http://localhost:8080/api/swagger-ui/index.html#Category/getCategories"
                                                    }
                                                }
                                            }
                                            """
                                    )
                            )
                    )
            }
    )
    public @interface List {
    }
}
