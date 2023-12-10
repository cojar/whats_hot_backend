package com.cojar.whats_hot_backend.global.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class PagedDataModel {

    private final List<DataModel> list;
    private final Integer page;
    private final Integer size;
    private final Integer firstPage;
    private final Integer prevPage;
    private final Integer nextPage;
    private final Integer lastPage;
    private final boolean first;
    private final boolean last;
    private final Integer totalPages;
    private final Long totalElements;

    public PagedDataModel(Page<DataModel> spotList) {
        this.list = spotList.getContent();
        this.page = spotList.getPageable().getPageNumber() + 1;
        this.size = spotList.getPageable().getPageSize();
        this.firstPage = 1;
        this.prevPage = spotList.hasPrevious() ? spotList.getPageable().getPageNumber() : null;
        this.nextPage = spotList.hasNext() ? spotList.getPageable().getPageNumber() + 2 : null;
        this.lastPage = spotList.getTotalPages();
        this.first = spotList.isFirst();
        this.last = spotList.isLast();
        this.totalPages = spotList.getTotalPages();
        this.totalElements = spotList.getTotalElements();
    }

    public static PagedDataModel of(Page<DataModel> spotList) {
        return new PagedDataModel(spotList);
    }

    public boolean hasPrev() {
        return this.prevPage != null;
    }

    public boolean hasNext() {
        return this.nextPage != null;
    }
}
