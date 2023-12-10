package com.cojar.whats_hot_backend.domain.member.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class MemberRequest {

    @Getter
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
    public static class Login {

        @NotBlank
        private String username;

        @NotBlank
        private String password;
    }

    @Getter
    public static class UpdatePassword {

        @NotBlank
        private String oldPassword;

        @NotBlank
        private String newPassword;

        @NotBlank
        private String newPasswordConfirm;
    }

    @Getter
    public static class FindUsername {

        @NotBlank
        private String email;
    }

    @Getter
    public static class FindPassword {

        @NotBlank
        private String username;

        @NotBlank
        private String email;
    }
}
