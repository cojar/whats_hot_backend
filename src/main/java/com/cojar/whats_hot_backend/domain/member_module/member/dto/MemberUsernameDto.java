package com.cojar.whats_hot_backend.domain.member_module.member.dto;

import com.cojar.whats_hot_backend.domain.member_module.member.entity.Member;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class MemberUsernameDto {

    private final String username;

    private MemberUsernameDto(Member member) {
        this.username = member.getUsername();
    }

    public static MemberUsernameDto of(Member member) {
        return new MemberUsernameDto(member);
    }
}
