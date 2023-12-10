package com.cojar.whats_hot_backend.domain.member.response;

import com.cojar.whats_hot_backend.domain.member.dto.MemberDto;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class MemberResponse {

    @Getter
    @AllArgsConstructor
    public static class Login {

        private final String accessToken;
    }

    @Getter
    @AllArgsConstructor
    public static class Me {

        @JsonUnwrapped
        private final MemberDto memberDto;
    }

    @Getter
    @AllArgsConstructor
    public static class FindUsername {

        private final String username;
    }
}
