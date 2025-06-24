package com.oide.fileData.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.time.Instant;

import com.oide.fileData.dto.FileDTO;

@Entity
@Table(name = "files")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The user who uploaded the file
    @Column(nullable = false)
    private Long userId;

    // Original filename (e.g., "document.pdf")
    @Column(nullable = false)
    private String originalFilename;

    // The stored filename on disk (e.g., "42_1623456789012_document.pdf")
    @Column(nullable = false, unique = true)
    private String storageFilename;

    // When the file was uploaded
    @Column(nullable = false)
    private Instant uploadTime;
    /**
     * Constructor for creating a new FileEntity with required fields.
     *
     * @param userId the ID of the user uploading the file
     * @param originalFilename the original name of the file
     * @param storageFilename the name under which the file is stored
     * @param uploadTime the time when the file was uploaded
     */

    

    // Getters and setters

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }
    public void setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
    }

    public String getStorageFilename() {
        return storageFilename;
    }
    public void setStorageFilename(String storageFilename) {
        this.storageFilename = storageFilename;
    }

    public Instant getUploadTime() {
        return uploadTime;
    }
    public void setUploadTime(Instant uploadTime) {
        this.uploadTime = uploadTime;
    }

    public FileDTO toDTO() {
        FileDTO dto = new FileDTO();
        dto.setFileId(this.id);
        dto.setUserId(this.userId);
        dto.setOriginalFilename(this.originalFilename);
        dto.setStorageFilename(this.storageFilename);
        dto.setUploadTime(this.uploadTime);
        return dto;
    }
}
