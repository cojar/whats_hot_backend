package com.cojar.whats_hot_backend.domain.spot_module.spot_image.entity;

import com.cojar.whats_hot_backend.domain.base_module.file.entity._File;
import com.cojar.whats_hot_backend.domain.spot_module.spot.entity.Spot;
import com.cojar.whats_hot_backend.global.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString(callSuper = true)
@Entity
public class SpotImage extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    private Spot spot;

    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true)
    private _File image;
}
