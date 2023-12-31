package com.cojar.whats_hot_backend.domain.member_module.member.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class MemberResponse {

    @Getter
    @AllArgsConstructor
    public static class FindUsername {

        private final String username;
    }
}
