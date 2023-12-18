package com.cojar.whats_hot_backend.domain.base_module.file.service;

import com.cojar.whats_hot_backend.domain.base_module.file.entity.SaveFile;
import com.cojar.whats_hot_backend.domain.base_module.file.repository.FileRepository;
import com.cojar.whats_hot_backend.domain.review_module.review.entity.Review;
import com.cojar.whats_hot_backend.domain.spot_module.spot.entity.Spot;
import com.cojar.whats_hot_backend.global.response.ResData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class FileService {

    private final FileRepository fileRepository;

    @Transactional
    public SaveFile create(Spot spot) {

        SaveFile saveFile = SaveFile.builder()
                .build();

        this.fileRepository.save(saveFile);

        return saveFile;
    }

    @Transactional
    public SaveFile create(Review review) {

        SaveFile saveFile = SaveFile.builder()
                .build();

        this.fileRepository.save(saveFile);

        return saveFile;
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
}
