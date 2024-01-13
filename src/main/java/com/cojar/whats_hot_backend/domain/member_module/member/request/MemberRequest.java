package com.cojar.whats_hot_backend.domain.member_module.member.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class MemberRequest {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Signup {

        @NotBlank
        private String username;

        @NotBlank
        private String password;

        @NotBlank
        private String passwordConfirm;

        @NotBlank
        private String email;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Login {

        @Schema(example = "user1")
        @NotBlank
        private String username;

        @Schema(example = "1234")
        @NotBlank
        private String password;

        public static Login of(String username, String password) {
            return new Login(username, password);
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UpdatePassword {

        @NotBlank
        private String oldPassword;

        @NotBlank
        private String newPassword;

        @NotBlank
        private String newPasswordConfirm;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class FindUsername {

        @NotBlank
        private String email;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class FindPassword {

        @NotBlank
        private String username;

        @NotBlank
        private String email;
    }
}
