package com.cojar.whats_hot_backend.domain.member.dto;

import com.cojar.whats_hot_backend.domain.member.entity.Member;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class MemberDto {

    private final Long id;

    private final LocalDateTime createDate;

    private final LocalDateTime modifyDate;

    private final String username;

    private final String email;

    private final List<String> authorities;

    private MemberDto(Member member) {
        this.id = member.getId();
        this.createDate = member.getCreateDate();
        this.modifyDate = member.getModifyDate();
        this.username = member.getUsername();
        this.email = member.getEmail();
        this.authorities = member.getAuthorities().stream()
                .map(e -> e.getAuthority()).toList();
    }

    public static MemberDto of(Member member) {
        return new MemberDto(member);
    }
}
