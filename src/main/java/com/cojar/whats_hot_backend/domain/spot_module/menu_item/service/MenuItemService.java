package com.cojar.whats_hot_backend.domain.spot_module.menu_item.service;

import com.cojar.whats_hot_backend.domain.spot_module.menu_item.dto.MenuItemDto;
import com.cojar.whats_hot_backend.domain.spot_module.menu_item.entity.MenuItem;
import com.cojar.whats_hot_backend.domain.spot_module.menu_item.repository.MenuItemRepository;
import com.cojar.whats_hot_backend.domain.spot_module.spot.entity.Spot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MenuItemService {

    private final MenuItemRepository menuItemRepository;

    public long count() {
        return this.menuItemRepository.count();
    }

    @Transactional
    public List<MenuItem> createAll(List<MenuItemDto> items, Spot spot) {

        if (items == null) return null;

        List<MenuItem> menuItems = items.stream()
                .map(item -> MenuItem.builder()
                        .name(item.getName())
                        .price(item.getPrice())
                        .spot(spot)
                        .build()
                )
                .collect(Collectors.toList());

        this.menuItemRepository.saveAll(menuItems);

        return menuItems;
    }

    public List<MenuItem> getAllBySpot(Spot spot) {
        return this.menuItemRepository.findAllBySpot(spot);
    }

    @Transactional
    public List<MenuItem> updateAll(List<MenuItemDto> items, Spot spot) {

        if (items == null) return null;

        List<MenuItem> menuItems = items.stream()
                .map(item -> MenuItem.builder()
                        .name(item.getName())
                        .price(item.getPrice())
                        .spot(spot)
                        .build()
                )
                .collect(Collectors.toList());

        this.menuItemRepository.deleteAll(spot.getMenuItems());
        this.menuItemRepository.saveAll(menuItems);

        return menuItems;
    }
}
