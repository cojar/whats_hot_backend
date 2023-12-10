package com.cojar.whats_hot_backend.domain.spot.api_response;

import com.cojar.whats_hot_backend.global.response.ResData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.hateoas.MediaTypes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public @interface SpotApiResponse {


    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "장소 등록",
            description = "성공 시 등록된 장소 정보를 반환한다",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "정상 응답",
                            content = @Content(mediaType = MediaTypes.HAL_JSON_VALUE,
                                    schema = @Schema(implementation = ResData.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                "status": "CREATED",
                                                "success": true,
                                                "code": "S-02-01",
                                                "message": "장소 등록이 완료되었습니다",
                                                "data": {
                                                    "id": 1,
                                                    "createDate": "2023-12-10T20:20:52.889114",
                                                    "modifyDate": "2023-12-10T20:20:52.889114",
                                                    "category": "맛집 > 2차 > 3차",
                                                    "address": "대전 서구 대덕대로 179",
                                                    "contact": "010-1234-5678",
                                                    "averageScore": 0,
                                                    "hashtags": [
                                                        {
                                                            "name": "해시태그1"
                                                        },
                                                        {
                                                            "name": "해시태그2"
                                                        }
                                                    ],
                                                    "menuItems": [
                                                        {
                                                            "name": "메뉴1",
                                                            "price": "10000원"
                                                        },
                                                        {
                                                            "name": "메뉴2",
                                                            "price": "20000원"
                                                        },
                                                        {
                                                            "name": "메뉴3",
                                                            "price": "30000원"
                                                        }
                                                    ],
                                                    "imageUri": [
                                                        "image uri",
                                                        "image uri",
                                                        "image uri"
                                                    ]
                                                },
                                                "_links": {
                                                    "self": {
                                                        "href": "http://localhost:8080/api/spots/1"
                                                    },
                                                    "profile": {
                                                        "href": "http://localhost:8080/swagger-ui/index.html#/Spot/create"
                                                    }
                                                }
                                            }
                                            """
                                    )
                            )
                    )
            }
    )
    public @interface Create {
    }
}
