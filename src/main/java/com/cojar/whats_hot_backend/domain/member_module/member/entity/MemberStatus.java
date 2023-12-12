package com.cojar.whats_hot_backend.domain.member_module.member.entity;

import lombok.Getter;

@Getter
public enum MemberStatus {

    ACTIVE("active"),
    INACTIVE("inactive"),
    ARCHIVED("archived"),
    PROHIBITED("prohibited");

    private String status;

    MemberStatus(String status) {
        this.status = status;
    }
}
