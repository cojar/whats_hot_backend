package com.cojar.whats_hot_backend.domain.file.repository;

import com.cojar.whats_hot_backend.domain.file.entity.SaveFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<SaveFile, Long> {
}
