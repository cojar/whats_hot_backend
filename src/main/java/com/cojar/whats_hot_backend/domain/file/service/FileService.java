package com.cojar.whats_hot_backend.domain.file.service;

import com.cojar.whats_hot_backend.domain.file.entity.SaveFile;
import com.cojar.whats_hot_backend.domain.file.repository.FileRepository;
import com.cojar.whats_hot_backend.domain.spot.entity.Spot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FileService {

    private final FileRepository fileRepository;

    public SaveFile create(Spot spot) {

        SaveFile saveFile = SaveFile.builder()
                .spot(spot != null ? spot : null)
                .build();

        this.fileRepository.save(saveFile);

        return saveFile;
    }
}
