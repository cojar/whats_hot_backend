package com.cojar.whats_hot_backend.domain.spot_module.spot.api_response;

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
                            responseCode = "201",
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
                                                    "createDate": "2024-01-16T22:26:29.302085",
                                                    "modifyDate": "2024-01-23T23:12:49.949402",
                                                    "category": "맛집 > 한식 > 족발/보쌈",
                                                    "name": "아저씨족발",
                                                    "address": "대전 서구 대덕대로 157-1",
                                                    "contact": "042-533-9888",
                                                    "averageScore": "4.00",
                                                    "hashtags": [
                                                        "족발",
                                                        "보쌈"
                                                    ],
                                                    "menuItems": [
                                                        {
                                                            "name": "오리지널족발(대)",
                                                            "price": "38,000원"
                                                        },
                                                        {
                                                            "name": "마늘족발(중)",
                                                            "price": "35,000원"
                                                        },
                                                        {
                                                            "name": "매운양념족발",
                                                            "price": "30,000원"
                                                        }
                                                    ],
                                                    "imageUri": [
                                                        "http://localhost:8080/file/spot/63b19d85-f727-478d-bc09-656b6fa9a6c3.png"
                                                    ],
                                                    "starred": 0,
                                                    "star": false
                                                },
                                                "_links": {
                                                    "self": {
                                                        "href": "http://localhost:8080/api/spots/1"
                                                    },
                                                    "profile": {
                                                        "href": "http://localhost:8080/swagger-ui/index.html#/Spot/createSpot"
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
            summary = "장소 목록 조회",
            description = "성공 시 요청한 장소 목록을 반환한다",
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
                                                "code": "S-02-02",
                                                "message": "요청하신 장소 목록을 반환합니다",
                                                "data": {
                                                    "list": [
                                                        {
                                                            "id": 1,
                                                            "category": "맛집 > 한식 > 족발/보쌈",
                                                            "name": "아저씨족발",
                                                            "address": "대전 서구 대덕대로 157-1",
                                                            "contact": "042-533-9888",
                                                            "averageScore": "4.00",
                                                            "hashtags": [
                                                                "족발",
                                                                "보쌈"
                                                            ],
                                                            "imageUri": [
                                                                "http://localhost:8080/file/spot/63b19d85-f727-478d-bc09-656b6fa9a6c3.png"
                                                            ],
                                                            "starred": 1,
                                                            "star": false,
                                                            "reviews": 4,
                                                            "_links": {
                                                                "self": {
                                                                    "href": "http://localhost:8080/api/spots/1"
                                                                }
                                                            }
                                                        },
                                                        {
                                                            "id": 2,
                                                            "category": "맛집 > 한식 > 족발/보쌈",
                                                            "name": "옥천순대매운족발",
                                                            "address": "대전 대덕구 송촌로 9",
                                                            "contact": "042-634-5882",
                                                            "averageScore": "3.75",
                                                            "hashtags": [
                                                                "순대",
                                                                "순댓국"
                                                            ],
                                                            "imageUri": [
                                                                "http://localhost:8080/file/spot/37d3ea82-7ed4-4426-a83e-8f0855c031ec.png"
                                                            ],
                                                            "starred": 0,
                                                            "star": false,
                                                            "reviews": 2,
                                                            "_links": {
                                                                "self": {
                                                                    "href": "http://localhost:8080/api/spots/2"
                                                                }
                                                            }
                                                        },
                                                        {
                                                            "id": 3,
                                                            "category": "맛집 > 한식 > 족발/보쌈",
                                                            "name": "화곡영양족발",
                                                            "address": "서울 강서구 초록마을로2길 48 1층 화곡영양족발",
                                                            "contact": "0507-1319-7914",
                                                            "averageScore": "3.00",
                                                            "hashtags": [
                                                                "족발",
                                                                "보쌈"
                                                            ],
                                                            "imageUri": [
                                                                "http://localhost:8080/file/spot/dc846117-0450-4788-b90b-62b7d134d94f.png"
                                                            ],
                                                            "starred": 0,
                                                            "star": false,
                                                            "reviews": 2,
                                                            "_links": {
                                                                "self": {
                                                                    "href": "http://localhost:8080/api/spots/3"
                                                                }
                                                            }
                                                        },
                                                        {
                                                            "id": 4,
                                                            "category": "맛집 > 일식 > 회",
                                                            "name": "동신수산",
                                                            "address": "대전 유성구 노은동로75번길 12",
                                                            "contact": "042-476-9968",
                                                            "averageScore": "2.50",
                                                            "hashtags": [
                                                                "생선회",
                                                                "알밥"
                                                            ],
                                                            "imageUri": [
                                                                "http://localhost:8080/file/spot/ec2ed55c-2654-4aeb-82ac-ffb2d1ce3c7a.png"
                                                            ],
                                                            "starred": 0,
                                                            "star": false,
                                                            "reviews": 2,
                                                            "_links": {
                                                                "self": {
                                                                    "href": "http://localhost:8080/api/spots/4"
                                                                }
                                                            }
                                                        },
                                                        {
                                                            "id": 5,
                                                            "category": "맛집 > 일식 > 회",
                                                            "name": "정치망",
                                                            "address": "대전 서구 도안대로 47",
                                                            "contact": "0507-1430-8188",
                                                            "averageScore": "2.50",
                                                            "hashtags": [
                                                                "생선회",
                                                                "초밥"
                                                            ],
                                                            "imageUri": [
                                                                "http://localhost:8080/file/spot/99c5c21b-8ca7-4eb9-befa-85ba863c914a.png"
                                                            ],
                                                            "starred": 0,
                                                            "star": false,
                                                            "reviews": 2,
                                                            "_links": {
                                                                "self": {
                                                                    "href": "http://localhost:8080/api/spots/5"
                                                                }
                                                            }
                                                        }
                                                    ],
                                                    "page": 1,
                                                    "size": 5,
                                                    "sort": [],
                                                    "firstPage": 1,
                                                    "nextPage": 2,
                                                    "lastPage": 21,
                                                    "first": true,
                                                    "last": false,
                                                    "totalPages": 21,
                                                    "totalElements": 102
                                                },
                                                "_links": {
                                                    "self": {
                                                        "href": "http://localhost:8080/api/spots?page=1&size=5&kw="
                                                    },
                                                    "profile": {
                                                        "href": "http://localhost:8080/swagger-ui/index.html#/Spot/list"
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
            summary = "장소 단건 조회",
            description = "성공 시 요청한 장소 정보를 반환한다",
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
                                                "code": "S-02-03",
                                                "message": "요청하신 장소 정보를 반환합니다",
                                                "data": {
                                                    "id": 1,
                                                    "createDate": "2024-01-16T22:26:29.302085",
                                                    "modifyDate": "2024-01-23T23:12:49.949402",
                                                    "category": "맛집 > 한식 > 족발/보쌈",
                                                    "name": "아저씨족발",
                                                    "address": "대전 서구 대덕대로 157-1",
                                                    "contact": "042-533-9888",
                                                    "averageScore": "4.00",
                                                    "hashtags": [
                                                        "족발",
                                                        "보쌈"
                                                    ],
                                                    "menuItems": [
                                                        {
                                                            "name": "오리지널족발(대)",
                                                            "price": "38,000원"
                                                        },
                                                        {
                                                            "name": "마늘족발(중)",
                                                            "price": "35,000원"
                                                        },
                                                        {
                                                            "name": "매운양념족발",
                                                            "price": "30,000원"
                                                        }
                                                    ],
                                                    "imageUri": [
                                                        "http://localhost:8080/file/spot/63b19d85-f727-478d-bc09-656b6fa9a6c3.png"
                                                    ],
                                                    "starred": 1,
                                                    "star": false,
                                                    "reviews": {
                                                        "list": [
                                                            {
                                                                "id": 1,
                                                                "createDate": "2024-01-16T22:26:30.301102",
                                                                "modifyDate": "2024-01-16T22:26:30.301102",
                                                                "spot": 1,
                                                                "author": "user1",
                                                                "visitDate": "2024-01-01T00:00:00",
                                                                "title": "다시 오고 싶어요!",
                                                                "content": "모든 메뉴가 다 맛있네요!",
                                                                "score": 4.5,
                                                                "hashtags": [
                                                                    "가족모임",
                                                                    "가족모임"
                                                                ],
                                                                "imageUri": [
                                                                    "http://localhost:8080/file/review/2ce6222e-ef9b-4d8d-8569-cd60aad2c2fc.png"
                                                                ],
                                                                "status": "public",
                                                                "validated": false,
                                                                "liked": 0,
                                                                "like": false,
                                                                "comments": [
                                                                    {
                                                                        "id": 1,
                                                                        "createDate": "2024-01-16T22:26:31.943803",
                                                                        "modifyDate": "2024-01-16T22:26:31.943803",
                                                                        "content": "감사합니다!",
                                                                        "liked": 0,
                                                                        "like": false,
                                                                        "author": "user2",
                                                                        "reviewId": 1
                                                                    },
                                                                    {
                                                                        "id": 103,
                                                                        "createDate": "2024-01-16T22:26:32.210582",
                                                                        "modifyDate": "2024-01-16T22:26:32.210582",
                                                                        "content": "댓글 감사합니다!",
                                                                        "liked": 0,
                                                                        "like": false,
                                                                        "author": "user2",
                                                                        "reviewId": 1,
                                                                        "tagId": 1
                                                                    },
                                                                    {
                                                                        "id": 205,
                                                                        "createDate": "2024-01-22T23:39:32.921921",
                                                                        "modifyDate": "2024-01-22T23:39:32.921921",
                                                                        "content": "댓글내용1",
                                                                        "liked": 0,
                                                                        "like": false,
                                                                        "author": "user1",
                                                                        "reviewId": 1,
                                                                        "tagId": 1
                                                                    }
                                                                ],
                                                                "_links": {
                                                                    "self": {
                                                                        "href": "http://localhost:8080/api/reviews/1"
                                                                    }
                                                                }
                                                            },
                                                            {
                                                                "id": 2,
                                                                "createDate": "2024-01-16T22:26:30.338797",
                                                                "modifyDate": "2024-01-16T22:26:30.338797",
                                                                "spot": 1,
                                                                "author": "user1",
                                                                "visitDate": "2024-01-01T00:00:00",
                                                                "title": "다시 오고 싶어요!",
                                                                "content": "가족들과 다시 오고 싶네요!",
                                                                "score": 2.5,
                                                                "hashtags": [
                                                                    "가족모임",
                                                                    "가족모임"
                                                                ],
                                                                "status": "public",
                                                                "validated": false,
                                                                "liked": 0,
                                                                "like": false,
                                                                "comments": [
                                                                    {
                                                                        "id": 2,
                                                                        "createDate": "2024-01-16T22:26:31.952077",
                                                                        "modifyDate": "2024-01-16T22:26:31.952077",
                                                                        "content": "저도 추천합니다!",
                                                                        "liked": 0,
                                                                        "like": false,
                                                                        "author": "user1",
                                                                        "reviewId": 2
                                                                    },
                                                                    {
                                                                        "id": 104,
                                                                        "createDate": "2024-01-16T22:26:32.213735",
                                                                        "modifyDate": "2024-01-16T22:26:32.213735",
                                                                        "content": "한 번 다녀오시길 추천해요!",
                                                                        "liked": 0,
                                                                        "like": false,
                                                                        "author": "user1",
                                                                        "reviewId": 2,
                                                                        "tagId": 2
                                                                    }
                                                                ],
                                                                "_links": {
                                                                    "self": {
                                                                        "href": "http://localhost:8080/api/reviews/2"
                                                                    }
                                                                }
                                                            },
                                                            {
                                                                "id": 203,
                                                                "createDate": "2024-01-17T16:24:29.647413",
                                                                "modifyDate": "2024-01-17T16:24:29.647413",
                                                                "spot": 1,
                                                                "author": "user1",
                                                                "visitDate": "2023-12-25T00:00:00",
                                                                "title": "리뷰 제목",
                                                                "content": "리뷰 내용",
                                                                "score": 4.5,
                                                                "hashtags": [
                                                                    "해시태그1",
                                                                    "해시태그2"
                                                                ],
                                                                "status": "public",
                                                                "validated": false,
                                                                "liked": 0,
                                                                "like": false,
                                                                "_links": {
                                                                    "self": {
                                                                        "href": "http://localhost:8080/api/reviews/203"
                                                                    }
                                                                }
                                                            },
                                                            {
                                                                "id": 204,
                                                                "createDate": "2024-01-17T16:42:05.492602",
                                                                "modifyDate": "2024-01-17T16:42:05.492602",
                                                                "spot": 1,
                                                                "author": "user1",
                                                                "visitDate": "2023-12-25T00:00:00",
                                                                "title": "리뷰 제목",
                                                                "content": "리뷰 내용",
                                                                "score": 4.5,
                                                                "hashtags": [
                                                                    "해시태그1",
                                                                    "해시태그2"
                                                                ],
                                                                "status": "public",
                                                                "validated": false,
                                                                "liked": 0,
                                                                "like": false,
                                                                "_links": {
                                                                    "self": {
                                                                        "href": "http://localhost:8080/api/reviews/204"
                                                                    }
                                                                }
                                                            }
                                                        ],
                                                        "page": 1,
                                                        "size": 20,
                                                        "sort": [
                                                            {
                                                                "direction": "desc",
                                                                "property": "liked"
                                                            },
                                                            {
                                                                "direction": "asc",
                                                                "property": "createDate"
                                                            }
                                                        ],
                                                        "firstPage": 1,
                                                        "lastPage": 1,
                                                        "first": true,
                                                        "last": true,
                                                        "totalPages": 1,
                                                        "totalElements": 4
                                                    }
                                                },
                                                "_links": {
                                                    "self": {
                                                        "href": "http://localhost:8080/api/spots/1"
                                                    },
                                                    "profile": {
                                                        "href": "http://localhost:8080/swagger-ui/index.html#/Spot/detail"
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
            summary = "장소 수정",
            description = "성공 시 수정된 장소 정보를 반환한다",
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
                                                "code": "S-02-04",
                                                "message": "장소 수정이 완료되었습니다",
                                                "data": {
                                                    "id": 1,
                                                    "createDate": "2024-01-16T22:26:29.302085",
                                                    "modifyDate": "2024-01-23T23:12:49.949402",
                                                    "category": "맛집 > 한식 > 족발/보쌈",
                                                    "name": "아저씨족발",
                                                    "address": "대전 서구 대덕대로 157-1",
                                                    "contact": "042-533-9888",
                                                    "averageScore": "4.00",
                                                    "hashtags": [
                                                        "족발",
                                                        "보쌈"
                                                    ],
                                                    "menuItems": [
                                                        {
                                                            "name": "오리지널족발(대)",
                                                            "price": "38,000원"
                                                        },
                                                        {
                                                            "name": "마늘족발(중)",
                                                            "price": "35,000원"
                                                        },
                                                        {
                                                            "name": "매운양념족발",
                                                            "price": "30,000원"
                                                        }
                                                    ],
                                                    "imageUri": [
                                                        "http://localhost:8080/file/spot/63b19d85-f727-478d-bc09-656b6fa9a6c3.png"
                                                    ],
                                                    "starred": 0,
                                                    "star": false
                                                },
                                                "_links": {
                                                    "self": {
                                                        "href": "http://localhost:8080/api/spots/1"
                                                    },
                                                    "profile": {
                                                        "href": "http://localhost:8080/swagger-ui/index.html#/Spot/updateSpot"
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
            summary = "장소 삭제",
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
                                                "code": "S-02-05",
                                                "message": "장소 삭제가 완료되었습니다",                                               
                                                "_links": {
                                                    "self": {
                                                        "href": "http://localhost:8080/api/spots"
                                                    },
                                                    "profile": {
                                                        "href": "http://localhost:8080/swagger-ui/index.html#/Spot/delete"
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
            summary = "장소 찜하기",
            description = "성공 시 해당 장소에 대한 요청 유저의 찜하기 상태를 변경한다",
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
                                                "code": "S-02-06",
                                                "message": "장소 찜하기 상태가 변경되었습니다",
                                                "data": {
                                                    "starred": 1,
                                                    "star": true
                                                },
                                                "_links": {
                                                    "self": {
                                                        "href": "http://localhost:8080/api/spots/1"
                                                    },
                                                    "profile": {
                                                        "href": "http://localhost:8080/api/swagger-ui/index.html#/Spot/starSpot"
                                                    }
                                                }
                                            }
                                            """
                                    )
                            )
                    )
            }
    )
    public @interface Star {
    }
}
