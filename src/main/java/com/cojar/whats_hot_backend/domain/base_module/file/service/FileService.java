package com.cojar.whats_hot_backend.domain.base_module.file.service;

import com.cojar.whats_hot_backend.domain.base_module.file.entity.FileDomain;
import com.cojar.whats_hot_backend.domain.base_module.file.entity._File;
import com.cojar.whats_hot_backend.domain.base_module.file.repository.FileRepository;
import com.cojar.whats_hot_backend.domain.review_module.review.entity.Review;
import com.cojar.whats_hot_backend.domain.spot_module.spot.entity.Spot;
import com.cojar.whats_hot_backend.global.errors.exception.ApiResponseException;
import com.cojar.whats_hot_backend.global.response.ResCode;
import com.cojar.whats_hot_backend.global.response.ResData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class FileService {

    @Value("${file.root.path}")
    private String root;

    private final FileRepository fileRepository;

    @Transactional
    public _File create(Spot spot) {

        _File saveFile = _File.builder()
                .build();

        this.fileRepository.save(saveFile);

        return saveFile;
    }

    @Transactional
    public _File create(Review review) {

        _File saveFile = _File.builder()
                .build();

        this.fileRepository.save(saveFile);

        return saveFile;
    }

    public _File create(MultipartFile _file, FileDomain domain) {

        Map<String, Object> fileBits = this.getFileBits(_file);
        String uuid = UUID.randomUUID().toString();
        String name = uuid + "." + fileBits.get("ext");
        String saveDirPath = root + File.separator + domain.getDomain();
        String saveFilePath = saveDirPath + File.separator + name;

        File target = new File(saveDirPath);
        if (!target.exists()) target.mkdirs();
        try {
            _file.transferTo(new File(saveFilePath));
        } catch (IOException e) {
            throw new ApiResponseException(ResData.of(ResCode.F_00_00_03));
        }

        _File file = _File.builder()
                .domain(domain)
                .uuid(uuid)
                .name(_file.getOriginalFilename())
                .size((Long) fileBits.get("size"))
                .ext(fileBits.get("ext").toString())
                .build();

        return file;
    }

    private Map<String, Object> getFileBits(MultipartFile file) {

        String[] fileBits = file.getContentType().split("/");
        String type = fileBits[0];
        String ext;

        if (fileBits.length == 2) {
            switch (fileBits[1].toLowerCase()) {
                case "jpg", "jpeg" -> ext = "jpg";
                case "png" -> ext = "png";
                default -> ext = "etc";
            }
        } else {
            ext = "etc";
        }

        return Map.of(
                "type", type,
                "ext", ext,
                "size", file.getSize()
        );
    }

    public ResData validateUnit(MultipartFile file) {

        ResData resData = this.validate(file);

        if (resData != null) throw new ApiResponseException(resData);

        return null;
    }

    public ResData validateAll(List<MultipartFile> files) {

        Errors errors = new BeanPropertyBindingResult(files, "file");

        ResData resData = ResData.reduceError(
                files.stream()
                        .map(file -> this.validate(file))
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList()),
                errors);

        if (resData != null) throw new ApiResponseException(resData);

        return null;
    }

    private ResData validate(MultipartFile file) {

        Map<String, Object> fileBits = this.getFileBits(file);
        Errors errors = new BeanPropertyBindingResult(file, "file");

        if (!fileBits.get("type").equals("image")) {

            errors.rejectValue("contentType", "not allowed", "content type is not allowed");

            return ResData.of(
                    ResCode.F_00_00_01,
                    errors
            );
        }

        if (fileBits.get("ext").equals("etc")) {

            errors.rejectValue("contentType", "not allowed", "file extension is not allowed");

            return ResData.of(
                    ResCode.F_00_00_02,
                    errors
            );
        }

        return null;
    }

    @Transactional
    public List<_File> createAll(List<MultipartFile> images, FileDomain fileDomain) {

        List<_File> files = images.stream()
                .map(image -> this.create(image, fileDomain))
                .collect(Collectors.toList());

        this.fileRepository.saveAll(files);

        return files;
    }

    @Transactional
    public void saveAll(List<_File> files) {
        if (files != null) this.fileRepository.saveAll(files);
    }
}
