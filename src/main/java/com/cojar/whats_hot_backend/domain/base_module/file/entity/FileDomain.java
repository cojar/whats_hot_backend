package com.cojar.whats_hot_backend.domain.base_module.file.entity;

import lombok.Getter;

@Getter
public enum FileDomain {

    MEMBER("member"),
    SPOT("spot"),
    REVIEW("review");

    private String domain;

    FileDomain(String domain) {
        this.domain = domain;
    }
}
