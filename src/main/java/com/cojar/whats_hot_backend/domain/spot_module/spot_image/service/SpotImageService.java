package com.cojar.whats_hot_backend.domain.spot_module.spot_image.service;

import com.cojar.whats_hot_backend.domain.base_module.file.entity._File;
import com.cojar.whats_hot_backend.domain.spot_module.spot.entity.Spot;
import com.cojar.whats_hot_backend.domain.spot_module.spot_image.entity.SpotImage;
import com.cojar.whats_hot_backend.domain.spot_module.spot_image.repository.SpotImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class SpotImageService {

    private final SpotImageRepository spotImageRepository;

    @Transactional
    public List<SpotImage> createAll(List<_File> files, Spot spot) {

        List<SpotImage> spotImages = files.stream()
                .map(file ->
                        SpotImage.builder()
                                .image(file)
                                .spot(spot)
                                .build()
                )
                .collect(Collectors.toList());

        this.spotImageRepository.saveAll(spotImages);

        return spotImages;
    }

    @Transactional
    public void saveAll(List<SpotImage> spotImages) {
        if (spotImages != null) this.spotImageRepository.saveAll(spotImages);
    }

    @Transactional
    public void saveAll(List<SpotImage> newSpotImages, List<SpotImage> oldSpotImages) {
        if (newSpotImages != null) {
            this.spotImageRepository.deleteAll(oldSpotImages);
            this.spotImageRepository.saveAll(newSpotImages);
        }
    }

    public List<SpotImage> getAllBySpot(Spot spot) {
        return this.spotImageRepository.findAllBySpot(spot);
    }

    public Long count() {
        return this.spotImageRepository.count();
    }
}
