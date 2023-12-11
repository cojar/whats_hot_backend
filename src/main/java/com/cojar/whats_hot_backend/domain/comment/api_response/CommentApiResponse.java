package com.cojar.whats_hot_backend.domain.comment.api_response;

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

public @interface CommentApiResponse {

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "댓글 등록",
            description = "성공 시 등록된 댓글 정보를 반환한다",
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
                                                "code": "S-04-01",
                                                "message": "댓글 등록이 완료되었습니다",
                                                "data": {
                                                    "id": 1,
                                                    "createDate": "2023-12-11T23:51:54.839192",
                                                    "modifyDate": "2023-12-11T23:51:54.839192",
                                                    "author": "user1",
                                                    "content": "댓글내용1"
                                                },
                                                "_links": {
                                                    "self": {
                                                        "href": "http://localhost:8080/api/comments/1"
                                                    },
                                                    "profile": {
                                                        "href": "http://localhost:8080/swagger-ui/index.html#/Comment/createComment"
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
            summary = "댓글 단건 조회",
            description = "성공 시 요청한 댓글 정보를 반환한다",
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
                                                "code": "S-04-02",
                                                "message": "요청하신 댓글 정보를 반환합니다",
                                                "data": {
                                                    "id": 1,
                                                    "createDate": "2023-12-11T23:51:54.839192",
                                                    "modifyDate": "2023-12-11T23:51:54.839192",
                                                    "author": "user1",
                                                    "content": "댓글내용1"
                                                },
                                                "_links": {
                                                    "self": {
                                                        "href": "http://localhost:8080/api/comments/1"
                                                    },
                                                    "profile": {
                                                        "href": "http://localhost:8080/swagger-ui/index.html#/Comment/createComment"
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
}
