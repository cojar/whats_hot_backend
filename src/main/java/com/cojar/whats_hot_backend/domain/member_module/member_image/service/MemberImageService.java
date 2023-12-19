package com.cojar.whats_hot_backend.domain.member_module.member_image.service;

import com.cojar.whats_hot_backend.domain.base_module.file.entity._File;
import com.cojar.whats_hot_backend.domain.member_module.member.entity.Member;
import com.cojar.whats_hot_backend.domain.member_module.member_image.entity.MemberImage;
import com.cojar.whats_hot_backend.domain.member_module.member_image.repository.MemberImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MemberImageService {

    private final MemberImageRepository memberImageRepository;

    @Transactional
    public MemberImage create(Member member, _File image) {

        MemberImage memberImage = MemberImage.builder()
                .member(member)
                .image(image)
                .build();

        this.memberImageRepository.save(memberImage);

        return memberImage;
    }
}
