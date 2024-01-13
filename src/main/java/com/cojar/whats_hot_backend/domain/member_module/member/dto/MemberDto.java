package com.cojar.whats_hot_backend.domain.member_module.member.dto;

import com.cojar.whats_hot_backend.domain.member_module.member.entity.Member;
import com.cojar.whats_hot_backend.global.util.AppConfig;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class MemberDto {

    private final Long id;

    private final LocalDateTime createDate;

    private final LocalDateTime modifyDate;

    private final String username;

    private final String email;

    private final String profileImageUri;

    private final List<String> authorities;

    private MemberDto(Member member) {
        this.id = member.getId();
        this.createDate = member.getCreateDate();
        this.modifyDate = member.getModifyDate();
        this.username = member.getUsername();
        this.email = member.getEmail();
        this.profileImageUri = member.getProfileImage() != null ? member.getProfileImage().getImage().toUri(AppConfig.getBaseFileURL()) : null;
        this.authorities = member.getAuthorities().stream()
                .map(e -> e.getAuthority()).toList();
    }

    public static MemberDto of(Member member) {
        return new MemberDto(member);
    }
}
