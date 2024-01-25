package com.cojar.whats_hot_backend.global.response;

import com.cojar.whats_hot_backend.global.util.AppConfig;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class PagedDataModel {

    private final List<DataModel> list;
    private final Integer page;
    private final Integer size;
    private final List<Map<String, String>> sort;
    private final Integer firstPage;
    private final Integer prevPage;
    private final Integer nextPage;
    private final Integer lastPage;
    private final boolean first;
    private final boolean last;
    private final Integer totalPages;
    private final Long totalElements;

    public PagedDataModel(Page<DataModel> pagedList) {
        this.list = pagedList.getContent();
        this.page = pagedList.getPageable().getPageNumber() + 1;
        this.size = pagedList.getPageable().getPageSize();
        this.sort = pagedList.getSort().stream()
                .map(s -> Map.of(
                        "property", AppConfig.toCamelCase(s.getProperty()),
                        "direction", s.getDirection().toString().toLowerCase()
                ))
                .collect(Collectors.toList());
        this.firstPage = 1;
        this.prevPage = pagedList.hasPrevious() ? pagedList.getPageable().getPageNumber() : null;
        this.nextPage = pagedList.hasNext() ? pagedList.getPageable().getPageNumber() + 2 : null;
        this.lastPage = pagedList.getTotalPages();
        this.first = pagedList.isFirst();
        this.last = pagedList.isLast();
        this.totalPages = pagedList.getTotalPages();
        this.totalElements = pagedList.getTotalElements();
    }

    public static PagedDataModel of(Page<DataModel> pagedList) {
        return new PagedDataModel(pagedList);
    }

    public boolean hasPrev() {
        return this.prevPage != null;
    }

    public boolean hasNext() {
        return this.nextPage != null;
    }
}
