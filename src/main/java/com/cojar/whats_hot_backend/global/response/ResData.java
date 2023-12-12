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

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class ResData<T> extends RepresentationModel {

    private final HttpStatus status;
    private final boolean success;
    private final String code;
    private final String message;
    private final T data;

    public ResData(HttpStatus status,
                   String code,
                   String message,
                   T data) {
        this.status = status;
        this.success = status.is2xxSuccessful();
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> ResData<T> of(HttpStatus status,
                                    String code,
                                    String message,
                                    T data,
                                    WebMvcLinkBuilder selfLink) {

        ResData resData = new ResData<>(status, code, message, data);

        if (data instanceof Errors) {
            resData.add(linkTo(methodOn(IndexController.class).index()).withRel("index"));
        }

        if (selfLink == null) return resData;

        resData.add(selfLink.withSelfRel());

        return resData;
    }

    public static <T> ResData<T> of(HttpStatus status,
                                    String code,
                                    String message) {

        return ResData.of(status, code, message, null, null);
    }

    public static <T> ResData<T> of(HttpStatus status,
                                    String code,
                                    String message,
                                    T data) {

        return ResData.of(status, code, message, data, null);
    }

    public static <T> ResData<T> of(HttpStatus status,
                                    String code,
                                    String message,
                                    WebMvcLinkBuilder selfLink) {

        return ResData.of(status, code, message, null, selfLink);
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
