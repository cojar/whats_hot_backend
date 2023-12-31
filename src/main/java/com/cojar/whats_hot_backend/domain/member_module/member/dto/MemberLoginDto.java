package com.cojar.whats_hot_backend.domain.member_module.member.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class MemberLoginDto {

    private final String accessToken;

    private MemberLoginDto(String accessToken) {
        this.accessToken = accessToken;
    }

    public static MemberLoginDto of(String accessToken) {
        return new MemberLoginDto(accessToken);
    }
}
