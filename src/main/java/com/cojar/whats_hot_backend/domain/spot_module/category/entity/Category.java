package com.cojar.whats_hot_backend.domain.spot_module.category.entity;

import com.cojar.whats_hot_backend.global.jpa.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString(callSuper = true)
@Entity
public class Category extends BaseEntity {

    private String name;

    private Integer depth;

    @ManyToOne(fetch = FetchType.LAZY)
    private Category parent;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Category> children;

    public String toLine() {

        Category category = this;
        String[] names = new String[category.getDepth()];

        while(category != null) {
            names[category.getDepth() - 1] = category.getName();
            category = category.getParent();
        }

        return String.join(" > ", names);
    }
}
