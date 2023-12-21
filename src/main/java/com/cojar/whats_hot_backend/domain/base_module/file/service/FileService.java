package com.cojar.whats_hot_backend.domain.base_module.file.service;

import com.cojar.whats_hot_backend.domain.base_module.file.entity.FileDomain;
import com.cojar.whats_hot_backend.domain.base_module.file.entity._File;
import com.cojar.whats_hot_backend.domain.base_module.file.repository.FileRepository;
import com.cojar.whats_hot_backend.domain.review_module.review.entity.Review;
import com.cojar.whats_hot_backend.domain.spot_module.spot.entity.Spot;
import com.cojar.whats_hot_backend.global.response.ResData;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

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

    @Transactional
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
            throw new RuntimeException("F-00-00-03", e);
        }

        _File file = _File.builder()
                .domain(domain)
                .uuid(uuid)
                .name(_file.getOriginalFilename())
                .size((Long) fileBits.get("size"))
                .ext(fileBits.get("ext").toString())
                .build();

        this.fileRepository.save(file);

        return file;
    }

    public ResData validate(MultipartFile file) {

        Map<String, Object> fileBits = this.getFileBits(file);
        Errors errors = new BeanPropertyBindingResult(file, "file");

        if (!fileBits.get("type").equals("image")) {

            errors.rejectValue("contentType", "not allowed", "content type is not allowed");

            return ResData.of(
                    HttpStatus.BAD_REQUEST,
                    "F-00-00-01",
                    "이미지 형식만 업로드할 수 있습니다",
                    errors
            );
        }

        if (fileBits.get("ext").equals("etc")) {

            errors.rejectValue("contentType", "not allowed", "file extension is not allowed");

            return ResData.of(
                    HttpStatus.BAD_REQUEST,
                    "F-00-00-02",
                    "JPG, JPEG, PNG 확장자만 업로드할 수 있습니다",
                    errors
            );
        }

        return null;
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

    @Transactional
    public List<_File> createAll(List<MultipartFile> images, FileDomain fileDomain) {
        return images.stream()
                .map(image -> {
                    _File file = this.create(image, fileDomain);
                    return this.fileRepository.save(file);
                })
                .collect(Collectors.toList());
    }
}
