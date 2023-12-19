package com.cojar.whats_hot_backend.domain.member_module.member.entity;

import com.cojar.whats_hot_backend.domain.member_module.member_image.entity.MemberImage;
import com.cojar.whats_hot_backend.global.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString(callSuper = true)
@Entity
public class Member extends BaseEntity {

    @Column(unique = true)
    private String username;

    private String password;

    @Column(unique = true)
    private String email;

    @OneToOne(fetch = FetchType.EAGER, mappedBy = "member", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private MemberImage profileImage;

    @Builder.Default
    @Enumerated(value = EnumType.STRING)
    private MemberStatus memberStatus = MemberStatus.ACTIVE;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(value = EnumType.STRING)
    private List<MemberRole> authorities;

    public boolean isLogout;

    public Collection<? extends GrantedAuthority> getAuthorities() {

        return this.authorities.stream()
                .map(a -> new SimpleGrantedAuthority(a.getType()))
                .collect(Collectors.toList());
    }

    public Map<String, Object> toClaims() {

        return Map.of(
                "id", this.getId(),
                "username", this.getUsername()
        );
    }

    public void updateProfileImage(MemberImage profileImage) {
        this.profileImage = profileImage;
    }
}
