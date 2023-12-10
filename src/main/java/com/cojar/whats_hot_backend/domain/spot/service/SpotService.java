package com.cojar.whats_hot_backend.domain.spot.service;

import com.cojar.whats_hot_backend.domain.category.entity.Category;
import com.cojar.whats_hot_backend.domain.hashtag.entity.Hashtag;
import com.cojar.whats_hot_backend.domain.spot.controller.SpotController;
import com.cojar.whats_hot_backend.domain.spot.dto.SpotListDto;
import com.cojar.whats_hot_backend.domain.spot.entity.Spot;
import com.cojar.whats_hot_backend.domain.spot.repository.SpotRepository;
import com.cojar.whats_hot_backend.global.response.DataModel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RequiredArgsConstructor
@Service
public class SpotService {

    private final SpotRepository spotRepository;

    public boolean hasNoSpot() {
        return this.spotRepository.count() == 0;
    }

    public Spot create(Category category, String address, String contact, List<Hashtag> hashtags) {

        Spot spot = Spot.builder()
                .category(category)
                .address(address)
                .contact(contact)
                .hashtags(hashtags).build();

        this.spotRepository.save(spot);

        return spot;
    }

    public Spot getSpotById(Long id) {
        return this.spotRepository.findById(id)
                .orElse(null);
    }

    public Page<DataModel> getSpotList(int page, int size) {

        Pageable pageable = PageRequest.of(page - 1, size);

        return this.spotRepository.findAll(pageable)
                .map(spot -> {
                    DataModel dataModel = DataModel.of(
                            SpotListDto.of(spot),
                            linkTo(SpotController.class).slash(spot.getId())
                    );
                    return dataModel;
                });
    }
}
