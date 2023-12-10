package com.cojar.whats_hot_backend.global.errors.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.validation.Errors;

import java.io.IOException;

@JsonComponent // objectMapper에 errorSerializer 등록
public class ErrorsSerializer extends JsonSerializer<Errors> {
    @Override
    public void serialize(Errors errors, JsonGenerator gen, SerializerProvider serializers) throws IOException {

        gen.writeStartArray();

        errors.getFieldErrors().forEach(err -> {
            try {
                gen.writeStartObject();
                gen.writeStringField("field", err.getField());
                gen.writeStringField("objectName", err.getObjectName());
                gen.writeStringField("code", err.getCode());
                gen.writeStringField("defaultMessage", err.getDefaultMessage());
                if (err.getRejectedValue() != null) {
                    gen.writeStringField("rejectedValue", err.getRejectedValue().toString());
                }
                gen.writeEndObject();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        errors.getGlobalErrors().forEach(err -> {
            try {
                gen.writeStartObject();
                gen.writeStringField("objectName", err.getObjectName());
                gen.writeStringField("code", err.getCode());
                gen.writeStringField("defaultMessage", err.getDefaultMessage());
                gen.writeEndObject();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        gen.writeEndArray();
    }
}
