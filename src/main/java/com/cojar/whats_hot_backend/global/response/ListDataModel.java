package com.cojar.whats_hot_backend.global.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class ListDataModel {

    private final List<DataModel> list;

    public ListDataModel(List<DataModel> list) {
        this.list = list;
    }

    public static ListDataModel of(List<DataModel> list) {
        return new ListDataModel(list);
    }
}
