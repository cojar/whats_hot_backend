package com.cojar.whats_hot_backend.domain.member_module.member.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class MemberTokenDto {

    private final String accessToken;

    private MemberTokenDto(String accessToken) {
        this.accessToken = accessToken;
    }

    public static MemberTokenDto of(String accessToken) {
        return new MemberTokenDto(accessToken);
    }
}
