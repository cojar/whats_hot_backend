package com.cojar.whats_hot_backend.domain.base_module.file.service;

import com.cojar.whats_hot_backend.domain.base_module.file.entity.SaveFile;
import com.cojar.whats_hot_backend.domain.base_module.file.repository.FileRepository;
import com.cojar.whats_hot_backend.domain.review_module.review.entity.Review;
import com.cojar.whats_hot_backend.domain.spot_module.spot.entity.Spot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class FileService {

    private final FileRepository fileRepository;

    @Transactional
    public SaveFile create(Spot spot) {

        SaveFile saveFile = SaveFile.builder()
                .build();

        this.fileRepository.save(saveFile);

        return saveFile;
    }

    @Transactional
    public SaveFile create(Review review) {

        SaveFile saveFile = SaveFile.builder()
                .build();

        this.fileRepository.save(saveFile);

        return saveFile;
    }
}
