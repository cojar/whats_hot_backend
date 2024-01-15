package com.cojar.whats_hot_backend.domain.base_module.file.repository;

import com.cojar.whats_hot_backend.domain.base_module.file.entity.FileDomain;
import com.cojar.whats_hot_backend.domain.base_module.file.entity._File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<_File, Long> {
    List<_File> findAllByDomain(FileDomain fileDomain);
}
