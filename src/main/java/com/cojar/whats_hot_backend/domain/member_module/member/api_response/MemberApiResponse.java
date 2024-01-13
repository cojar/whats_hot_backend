package com.cojar.whats_hot_backend.domain.member_module.member.api_response;

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

public @interface MemberApiResponse {

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "회원 가입",
            description = "회원 가입을 진행한다",
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
                                                "code": "S-01-01",
                                                "message": "회원가입을 완료했습니다",
                                                "data": {
                                                    "id": 1,
                                                    "createDate": "2023-12-20T01:33:52.377973",
                                                    "modifyDate": "2023-12-20T01:33:52.377973",
                                                    "username": "test",
                                                    "email": "test@test.io",
                                                    "profileImageUri": "http://localhost:8080/file/member/d6c2a791-bf1d-479d-ab7f-80d06148172b.png",
                                                    "authorities": [
                                                        "user"
                                                    ]
                                                },
                                                "_links": {
                                                    "self": {
                                                        "href": "http://localhost:8080/api/members/login"
                                                    },
                                                    "profile": {
                                                        "href": "http://localhost:8080/swagger-ui/index.html#/Member/signup"
                                                    }
                                                }
                                            }
                                            """
                                    )
                            )
                    )
            }
    )
    public @interface Signup {
    }

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "회원 로그인",
            description = "성공 시 액세스 토큰을 발급한다",
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
                                                "code": "S-01-02",
                                                "message": "액세스 토큰이 생성되었습니다",
                                                "data": {
                                                    "accessToken": "accessToken"
                                                },
                                                "_links": {
                                                    "self": {
                                                        "href": "http://localhost:8080/api/index"
                                                    },
                                                    "profile": {
                                                        "href": "http://localhost:8080/swagger-ui/index.html#/Member/login"
                                                    }
                                                }
                                            }
                                            """
                                    )
                            )
                    )
            }
    )
    public @interface Login {
    }

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "회원 로그아웃",
            description = "성공 시 회원을 로그아웃 상태로 변경한다",
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
                                                "code": "S-01-03",
                                                "message": "로그아웃이 완료되었습니다",
                                                "_links": {
                                                    "self": {
                                                        "href": "http://localhost:8080/api/index"
                                                    },
                                                    "profile": {
                                                        "href": "http://localhost:8080/swagger-ui/index.html#/Member/logout"
                                                    }
                                                }
                                            }
                                            """
                                    )
                            )
                    )
            }
    )
    public @interface Logout {
    }

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "회원 정보 조회",
            description = "현재 로그인된 회원 정보를 반환한다",
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
                                                "code": "S-01-04",
                                                "message": "로그인된 회원 정보를 반환합니다",
                                                "data": {
                                                    "id": 1,
                                                    "createDate": "2023-12-06T16:30:32.952443",
                                                    "modifyDate": "2023-12-06T16:30:32.952443",
                                                    "username": "user",
                                                    "email": "user@test.com",
                                                    "authorities": [
                                                        "USER"
                                                    ]
                                                },
                                                "_links": {
                                                    "self": {
                                                        "href": "http://localhost:8080/api/members/me"
                                                    },
                                                    "profile": {
                                                        "href": "http://localhost:8080/swagger-ui/index.html#/Member/me"
                                                    }
                                                }
                                            }
                                            """
                                    )
                            )
                    )
            }
    )
    public @interface Me {
    }

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "회원 비밀번호 변경",
            description = "현재 로그인된 회원의 비밀번호를 변경한다",
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
                                                "code": "S-01-05",
                                                "message": "비밀번호 변경을 완료했습니다",
                                                "_links": {
                                                    "self": {
                                                        "href": "http://localhost:8080/api/members/me"
                                                    },
                                                    "profile": {
                                                        "href": "http://localhost:8080/swagger-ui/index.html#/Member/updatePassword"
                                                    }
                                                }
                                            }
                                            """
                                    )
                            )
                    )
            }
    )
    public @interface UpdatePassword {
    }

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "회원 아이디 찾기",
            description = "아이디 찾기 성공 시 회원의 아이디를 반환한다",
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
                                                "code": "S-01-06",
                                                "message": "요청하신 아이디를 반환합니다",
                                                "data": {
                                                    "username": "user"
                                                },
                                                "_links": {
                                                    "self": {
                                                        "href": "http://localhost:8080/api/members/login"
                                                    },
                                                    "profile": {
                                                        "href": "http://localhost:8080/swagger-ui/index.html#/Member/findUsername"
                                                    }
                                                }
                                            }
                                            """
                                    )
                            )
                    )
            }
    )
    public @interface FindUsername {
    }

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "회원 비밀번호 찾기",
            description = "비밀번호 찾기 성공 시 임시비밀번호를 부여하고 메일로 발송한다",
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
                                                "code": "S-01-07",
                                                "message": "이메일로 임시비밀번호를 발송했습니다",
                                                "_links": {
                                                    "self": {
                                                        "href": "http://localhost:8080/api/members/login"
                                                    },
                                                    "profile": {
                                                        "href": "http://localhost:8080/swagger-ui/index.html#/Member/findPassword"
                                                    }
                                                }
                                            }
                                            """
                                    )
                            )
                    )
            }
    )
    public @interface FindPassword {
    }
}
