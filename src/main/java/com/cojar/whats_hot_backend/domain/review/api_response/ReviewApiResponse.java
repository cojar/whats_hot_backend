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
                                                    "validated": true,
                                                    "liked": 0
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
                                                            "liked": 0,
                                                            "comments": [
                                                                {
                                                                    "id": 1,
                                                                    "createDate": "2023-12-12T00:27:10.243699",
                                                                    "modifyDate": "2023-12-12T00:27:10.243699",
                                                                    "author": "user1",
                                                                    "content": "댓글내용1",
                                                                    "liked": 0
                                                              }
                                                            ],
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
                                                            "liked": 0,
                                                            "comments": [
                                                                {
                                                                    "id": 2,
                                                                    "createDate": "2023-12-12T00:27:10.243699",
                                                                    "modifyDate": "2023-12-12T00:27:10.243699",
                                                                    "author": "user1",
                                                                    "content": "댓글내용2",
                                                                    "liked": 0
                                                              }
                                                            ],
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

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "리뷰 단건 조회",
            description = "성공 시 요청한 리뷰 정보를 반환한다",
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
                                                "code": "S-03-03",
                                                "message": "요청하신 리뷰 정보를 반환합니다",
                                                "data": {
                                                    "id": 1,
                                                    "createDate": "2023-12-11T23:09:31.343276",
                                                    "modifyDate": "2023-12-11T23:09:31.343276",
                                                    "author": "user1",
                                                    "visitDate": "2023-12-11T23:09:31.342859",
                                                    "title": "리뷰제목1",
                                                    "content": "리뷰내용1",
                                                    "score": 4.5,
                                                    "imageUri": [
                                                        "image uri"
                                                    ],
                                                    "status": "public",
                                                    "validated": true,
                                                    "liked": 0,
                                                    "comments": [
                                                        {
                                                            "id": 1,
                                                            "createDate": "2023-12-12T00:27:10.243699",
                                                            "modifyDate": "2023-12-12T00:27:10.243699",
                                                            "author": "user1",
                                                            "content": "댓글내용1",
                                                            "liked": 0
                                                        }
                                                    ],
                                                },
                                                "_links": {
                                                    "self": {
                                                        "href": "http://localhost:8080/api/reviews/1"
                                                    },
                                                    "profile": {
                                                        "href": "http://localhost:8080/swagger-ui/index.html#/Review/getReview"
                                                    }
                                                }
                                            }
                                            """
                                    )
                            )
                    )
            }
    )
    public @interface Detail {
    }

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "리뷰 수정",
            description = "성공 시 수정된 리뷰 정보를 반환한다",
            security = @SecurityRequirement(name = "bearerAuth"),
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
                                                "code": "S-03-04",
                                                "message": "리뷰 수정이 완료되었습니다",
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
                                                    "validated": true,
                                                    "liked": 0,
                                                    "comments": [
                                                        {
                                                            "id": 1,
                                                            "createDate": "2023-12-12T00:27:10.243699",
                                                            "modifyDate": "2023-12-12T00:27:10.243699",
                                                            "author": "user1",
                                                            "content": "댓글내용1",
                                                            "liked": 0
                                                        }
                                                    ],
                                                },
                                                "_links": {
                                                    "self": {
                                                        "href": "http://localhost:8080/api/reviews/1"
                                                    },
                                                    "profile": {
                                                        "href": "http://localhost:8080/swagger-ui/index.html#/Review/updateReview"
                                                    }
                                                }
                                            }
                                            """
                                    )
                            )
                    )
            }
    )
    public @interface Update {
    }

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "리뷰 삭제",
            description = "성공 시 해당 장소를 삭제한다",
            security = @SecurityRequirement(name = "bearerAuth"),
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
                                                "code": "S-03-05",
                                                "message": "리뷰 삭제가 완료되었습니다",
                                                "_links": {
                                                    "self": {
                                                        "href": "http://localhost:8080/api/reviews"
                                                    },
                                                    "profile": {
                                                        "href": "http://localhost:8080/swagger-ui/index.html#/Review/deleteReview"
                                                    }
                                                }
                                            }
                                            """
                                    )
                            )
                    )
            }
    )
    public @interface Delete {
    }

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "리뷰 좋아요",
            description = "성공 시 해당 리뷰에 대한 요청 유저의 좋아요 상태를 변경한다",
            security = @SecurityRequirement(name = "bearerAuth"),
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
                                                "code": "S-03-06",
                                                "message": "리뷰 좋아요 상태가 변경되었습니다",
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
                                                    "validated": true,
                                                    "liked": 0,
                                                    "comments": [
                                                        {
                                                            "id": 1,
                                                            "createDate": "2023-12-12T00:27:10.243699",
                                                            "modifyDate": "2023-12-12T00:27:10.243699",
                                                            "author": "user1",
                                                            "content": "댓글내용1",
                                                            "liked": 0
                                                        }
                                                    ],
                                                },
                                                "_links": {
                                                    "self": {
                                                        "href": "http://localhost:8080/api/reviews/1"
                                                    },
                                                    "profile": {
                                                        "href": "http://localhost:8080/swagger-ui/index.html#/Review/updateReview"
                                                    }
                                                }
                                            }
                                            """
                                    )
                            )
                    )
            }
    )
    public @interface Like {
    }
}
