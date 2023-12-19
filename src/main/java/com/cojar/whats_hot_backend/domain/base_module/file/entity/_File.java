package com.cojar.whats_hot_backend.domain.base_module.file.entity;

import com.cojar.whats_hot_backend.global.jpa.BaseEntity;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.File;

@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString(callSuper = true)
@Entity
public class _File extends BaseEntity {

    private FileDomain domain;

    private String uuid;

    private String name;

    private Long size;

    private String ext;

    public String toUri(String origin) {
        return origin
                + File.separator + this.domain.getDomain()
                + File.separator + this.uuid + "." + this.ext;
    }
}
