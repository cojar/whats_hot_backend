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

    @Transactional
    public MenuItem create(String name, String price, Spot spot) {

        MenuItem menuItem = MenuItem.builder()
                .name(name)
                .price(price)
                .spot(spot)
                .build();

        this.menuItemRepository.save(menuItem);

        return menuItem;
    }

    @Transactional
    public List<MenuItem> createAll(List<MenuItemDto> items, Spot spot) {

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

    @Transactional
    public void saveAll(List<MenuItem> menuItems) {
        if (menuItems != null) this.menuItemRepository.saveAll(menuItems);
    }

    @Transactional
    public void saveAll(List<MenuItem> newMenuItems, List<MenuItem> oldMenuItems) {
        if (newMenuItems != null) {
            this.menuItemRepository.deleteAll(oldMenuItems);
            this.menuItemRepository.saveAll(newMenuItems);
        }
    }

    public List<MenuItem> getAllBySpot(Spot spot) {
        return this.menuItemRepository.findAllBySpot(spot);
    }

    public Long count() {
        return this.menuItemRepository.count();
    }
}
