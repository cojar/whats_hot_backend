package com.cojar.whats_hot_backend.global.response;

import com.cojar.whats_hot_backend.domain.index_module.index.controller.IndexController;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;

import java.net.URI;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class ResData<T> extends RepresentationModel {

    private final HttpStatus status;
    private final boolean success;
    private final Object code;
    private final Object message;
    private final T data;

    public ResData(ResCode resCode,
                   T data) {
        this.status = resCode.getStatus();
        this.success = status.is2xxSuccessful();
        this.code = resCode.getCode();
        this.message = resCode.getMessage();
        this.data = data;
    }

    public ResData(Set<ResCode> resCodeSet,
                   T data) {
        this.status = resCodeSet.stream()
                .map(resCode -> resCode.getStatus())
                .collect(Collectors.toCollection(LinkedHashSet::new))
                .stream().findFirst().orElse(null);
        this.success = status.is2xxSuccessful();
        this.code = resCodeSet.stream()
                .map(resCode -> resCode.getCode())
                .collect(Collectors.toCollection(LinkedHashSet::new));
        this.message = resCodeSet.stream()
                .map(resCode -> resCode.getMessage())
                .collect(Collectors.toCollection(LinkedHashSet::new));
        this.data = data;
    }

    public static <T> ResData<T> of(ResCode resCode,
                                    T data,
                                    WebMvcLinkBuilder selfLink) {

        ResData resData = new ResData<>(resCode, data);

        if (data instanceof Errors) {
            resData.add(linkTo(methodOn(IndexController.class).index()).withRel("index"));
        }

        if (selfLink == null) return resData;

        resData.add(selfLink.withSelfRel());

        return resData;
    }

    public static <T> ResData<T> of(Set<ResCode> resCodeSet,
                                    T data,
                                    WebMvcLinkBuilder selfLink) {

        ResData resData = new ResData<>(resCodeSet, data);

        if (data instanceof Errors) {
            resData.add(linkTo(methodOn(IndexController.class).index()).withRel("index"));
        }

        if (selfLink == null) return resData;

        resData.add(selfLink.withSelfRel());

        return resData;
    }

    public static <T> ResData<T> of(ResCode resCode) {

        return ResData.of(resCode, null, null);
    }

    public static <T> ResData<T> of(ResCode resCode,
                                    T data) {

        return ResData.of(resCode, data, null);
    }

    public static <T> ResData<T> of(ResCode resCode,
                                    WebMvcLinkBuilder selfLink) {

        return ResData.of(resCode, null, selfLink);
    }

    private static <T> ResData<T> of(Set<ResCode> resCode,
                                     T data) {

        return ResData.of(resCode, data, null);
    }

    public static ResData reduceError(List<ResData> resDataList, Errors errors) {

        Set<ResCode> resCodeSet = new TreeSet<>();

        resDataList.stream()
                .forEach(resData -> {
                    resCodeSet.add(ResCode.fromCode(resData.getCode().toString()));
                    Errors resErrors = (Errors) resData.getData();
                    errors.addAllErrors(resErrors);
                });

        if (resCodeSet.size() == 0) return null;

        return ResData.of(
                resCodeSet,
                errors
        );
    }

    @JsonIgnore
    public Link getSelf() {
        return this.getRequiredLink("self");
    }

    @JsonIgnore
    public URI getSelfUri() {
        return this.getSelf().toUri();
    }
}
