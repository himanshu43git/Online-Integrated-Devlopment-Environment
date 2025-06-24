package com.oide.fileData.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.oide.fileData.entities.FileEntity;

public interface FileRepository extends JpaRepository<FileEntity, Long> {
    
}
