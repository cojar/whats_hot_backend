package com.cojar.whats_hot_backend.domain.base_module.file.repository;

import com.cojar.whats_hot_backend.domain.base_module.file.entity._File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<_File, Long> {
}
