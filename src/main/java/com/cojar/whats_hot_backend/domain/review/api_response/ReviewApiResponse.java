package com.cojar.whats_hot_backend.domain.review.api_response;

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

public @interface ReviewApiResponse {

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "리뷰 등록",
            description = "성공 시 등록된 리뷰 정보를 반환한다",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "정상 응답",
                            content = @Content(mediaType = MediaTypes.HAL_JSON_VALUE,
                                    schema = @Schema(implementation = ResData.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                "status": "CREATED",
                                                "success": true,
                                                "code": "S-03-01",
                                                "message": "리뷰 등록이 완료되었습니다",
                                                "data": {
                                                    "id": 1,
                                                    "createDate": "2023-12-11T22:43:34.502462",
                                                    "modifyDate": "2023-12-11T22:43:34.502462",
                                                    "author": "user1",
                                                    "visitDate": "2023-12-11T22:43:34.500966",
                                                    "title": "리뷰제목1",
                                                    "content": "리뷰내용1",
                                                    "score": 4.5,
                                                    "imageUri": [
                                                        "image uri"
                                                    ],
                                                    "status": "public",
                                                    "validated": true
                                                },
                                                "_links": {
                                                    "self": {
                                                        "href": "http://localhost:8080/api/reviews/1"
                                                    },
                                                    "profile": {
                                                        "href": "http://localhost:8080/swagger-ui/index.html#/Review/createReview"
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

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "리뷰 목록 조회",
            description = "성공 시 요청한 리뷰 목록을 반환한다",
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
                                                "code": "S-03-02",
                                                "message": "요청하신 리뷰 목록을 반환합니다",
                                                "data": {
                                                    "list": [
                                                        {
                                                            "id": 1,
                                                            "createDate": "2023-12-11T22:56:23.991229",
                                                            "modifyDate": "2023-12-11T22:56:23.991229",
                                                            "author": "user1",
                                                            "visitDate": "2023-12-11T22:56:23.990797",
                                                            "title": "리뷰제목1",
                                                            "content": "리뷰내용1",
                                                            "score": 4.5,
                                                            "imageUri": [
                                                                "image uri"
                                                            ],
                                                            "status": "public",
                                                            "validated": true,
                                                            "_links": {
                                                                "self": {
                                                                    "href": "http://localhost:8080/api/spots/1"
                                                                }
                                                            }
                                                        },
                                                        {
                                                            "id": 2,
                                                            "createDate": "2023-12-11T22:56:23.994093",
                                                            "modifyDate": "2023-12-11T22:56:23.994093",
                                                            "author": "user1",
                                                            "visitDate": "2023-12-11T22:56:23.993971",
                                                            "title": "리뷰제목2",
                                                            "content": "리뷰내용2",
                                                            "score": 4.5,
                                                            "imageUri": [
                                                                "image uri"
                                                            ],
                                                            "status": "public",
                                                            "validated": true,
                                                            "_links": {
                                                                "self": {
                                                                    "href": "http://localhost:8080/api/spots/2"
                                                                }
                                                            }
                                                        }
                                                    ],
                                                    "page": 1,
                                                    "size": 2,
                                                    "firstPage": 1,
                                                    "lastPage": 1,
                                                    "first": true,
                                                    "last": true,
                                                    "totalPages": 1,
                                                    "totalElements": 2
                                                },
                                                "_links": {
                                                    "self": {
                                                        "href": "http://localhost:8080/api/reviews?page=1&size=2"
                                                    },
                                                    "profile": {
                                                        "href": "http://localhost:8080/swagger-ui/index.html#/Review/getReviewList"
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
