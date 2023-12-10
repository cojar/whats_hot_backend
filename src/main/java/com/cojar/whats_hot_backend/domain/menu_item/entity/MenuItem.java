package com.cojar.whats_hot_backend.domain.menu_item.entity;

import com.cojar.whats_hot_backend.domain.spot.entity.Spot;
import com.cojar.whats_hot_backend.global.jpa.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString(callSuper = true)
@Entity
public class MenuItem extends BaseEntity {

    private String name;

    private String price;

    @ManyToOne(fetch = FetchType.LAZY)
    private Spot spot;
}
