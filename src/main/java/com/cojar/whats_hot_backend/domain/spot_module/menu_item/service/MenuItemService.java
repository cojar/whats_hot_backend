package com.cojar.whats_hot_backend.domain.spot_module.menu_item.service;

import com.cojar.whats_hot_backend.domain.spot_module.menu_item.entity.MenuItem;
import com.cojar.whats_hot_backend.domain.spot_module.menu_item.repository.MenuItemRepository;
import com.cojar.whats_hot_backend.domain.spot_module.spot.entity.Spot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MenuItemService {

    private final MenuItemRepository menuItemRepository;

    public MenuItem create(String name, String price, Spot spot) {

        MenuItem menuItem = MenuItem.builder()
                .name(name)
                .price(price)
                .spot(spot)
                .build();

        this.menuItemRepository.save(menuItem);

        return menuItem;
    }
}
