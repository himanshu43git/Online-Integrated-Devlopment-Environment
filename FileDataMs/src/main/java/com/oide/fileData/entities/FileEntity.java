package com.oide.fileData.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

import com.oide.fileData.dto.FileDTO;

@Document(collection = "files")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileEntity {

    /** Mongo’s document ID (will be an ObjectId by default) */
    @Id
    private String id;

    /** The user who uploaded the file */
    @Field("userId")
    private Long userId;

    /** Original filename (e.g., "document.pdf") */
    @Field("originalFilename")
    private String originalFilename;

    /** Optional in-DB file content (e.g. Base64 or text) */
    @Field("fileContent")
    private String fileContent;

    /** Stored filename on disk (e.g., "42_1623456789012_document.pdf") */
    @Indexed(unique = true)
    @Field("storageFilename")
    private String storageFilename;

    /** When the file was uploaded */
    @Field("uploadTime")
    private Instant uploadTime;

    /**
     * Convenience ctor for creating a new FileEntity (before Mongo assigns its ID).
     */
    public FileEntity(Long userId,
                      String originalFilename,
                      String storageFilename,
                      Instant uploadTime) {
        this.userId = userId;
        this.originalFilename = originalFilename;
        this.storageFilename = storageFilename;
        this.uploadTime = uploadTime;
    }

    /**
     * Convert to DTO.
     */
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