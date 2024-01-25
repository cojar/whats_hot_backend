package com.cojar.whats_hot_backend.global.util;

import com.cojar.whats_hot_backend.domain.base_module.file.entity.FileDomain;
import com.cojar.whats_hot_backend.domain.member_module.member.entity.Member;
import com.cojar.whats_hot_backend.global.response.ResData;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.security.SecureRandom;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Configuration
public class AppConfig {

    private static String fileOriginPath;

    private static String fileRootPath;

    @Value("${file.origin.path}")
    public void setFileOriginPath(String fileOriginPath) {
        AppConfig.fileOriginPath = fileOriginPath;
    }

    @Value("${file.root.path}")
    public void setFileRootPath(String fileRootPath) {
        AppConfig.fileRootPath = fileRootPath;
    }

    @Bean
    public ModelMapper modelMapper() {

        ModelMapper modelMapper = new ModelMapper();

        modelMapper.getConfiguration()
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
                .setFieldMatchingEnabled(true);

        return modelMapper;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public static String getBaseURL() {
        return ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
    }

    public static String getIndexURL() {
        return getBaseURL() + "/api/swagger-ui/index.html";
    }

    public static String getBaseFileURL() {
        return getBaseURL() + fileOriginPath;
    }

    public static String getBaseFilePath() {
        return fileRootPath;
    }

    public static String getMediaType(String fileName1) {
        String ext = fileName1.split("\\.")[1].toLowerCase();
        switch (ext) {
            case "jpg", "jpeg" -> {
                return MediaType.IMAGE_JPEG_VALUE;
            }
            case "png" -> {
                return MediaType.IMAGE_PNG_VALUE;
            }
            default -> {
                return MediaType.ALL_VALUE;
            }
        }
    }

    public static String getRandomPassword() {

        String candidateCode = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ!@#$%^&";
        SecureRandom secureRandom = new SecureRandom();

        String code = "";
        int len = 24;
        for (int i = 0; i < len; i++) {
            int index = secureRandom.nextInt(candidateCode.length());
            code += candidateCode.charAt(index);
        }

        return code;
    }

    public static String getQueryString(MultiValueMap<String, String> params) {
        return params.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + String.join("&", entry.getValue()))
                .collect(Collectors.joining("&"));
    }

    public static User toUser(Member member) {
        return new User(member.getUsername(), member.getPassword(), member.getAuthorities());
    }

    public static String toCamelCase(String property) {
        String[] bits = property.split("_");
        return IntStream.range(0, bits.length)
                .mapToObj(i -> i != 0 ? bits[i].substring(0, 1).toUpperCase() + bits[i].substring(1) : bits[i])
                .collect(Collectors.joining());
    }

    public static Errors getMockErrors() {
        return new BeanPropertyBindingResult(null, "request");
    }

    public static Errors getMockErrors(String objectName) {
        return new BeanPropertyBindingResult(null, objectName);
    }

    public static Errors getMockErrors(Object object, String objectName) {
        return new BeanPropertyBindingResult(object, objectName);
    }

    public static String toJson(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    public static Map<String, Object> toMap(String jsonString) {
        try {
            return new ObjectMapper().readValue(jsonString, LinkedHashMap.class);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    public static String responseSerialize(ResData resData) {

        try {
            StringWriter writer = new StringWriter();
            JsonGenerator gen = new JsonFactory().createGenerator(writer);

            gen.writeStartObject();

            gen.writeStringField("status", resData.getStatus().name());
            gen.writeBooleanField("success", resData.isSuccess());
            gen.writeStringField("code", resData.getCode().toString());
            gen.writeStringField("message", resData.getMessage().toString());

            if (!resData.getLinks().isEmpty()) {
                gen.writeFieldName("_links");
                gen.writeStartObject();

                resData.getLinks().forEach(link -> {
                    try {
                        gen.writeFieldName(link.getRel().toString());
                        gen.writeStartObject();
                        gen.writeStringField("href", link.getHref());
                        gen.writeEndObject();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });

                gen.writeEndObject();
            }

            gen.writeEndObject();
            gen.close();
            writer.close();
            return writer.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean fileExists() {

        File member = new File(getDomainFilePath(FileDomain.MEMBER));
        File spot = new File(getDomainFilePath(FileDomain.SPOT));
        File review = new File(getDomainFilePath(FileDomain.REVIEW));

        if (!member.exists() || !member.isDirectory() || !spot.exists() || !spot.isDirectory() || !review.exists() || !review.isDirectory()) {
            System.out.println("삭제 미실행");
            return false;
        }
        return member.listFiles() != null || spot.listFiles() != null || review.listFiles() != null;
    }

    public static String getDomainFilePath(FileDomain fileDomain) {
        return getBaseFilePath() + File.separator + fileDomain.getDomain();
    }

    public static boolean isValidateRegion(String region) {

        List<String> regions = List.of("", "강원", "경기", "경남", "경북", "광주",
                "대구", "대전", "부산", "서울", "세종",
                "울산", "인천", "전남", "전북", "제주",
                "충남", "충북");

        return regions.contains(region);
    }
}
