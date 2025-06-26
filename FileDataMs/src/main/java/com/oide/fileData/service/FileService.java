package com.oide.fileData.service;

import com.oide.fileData.dto.FileDTO;

public interface FileService {
    
    /**
     * Uploads a file and returns its metadata.
     *
     * @param fileDTO The file metadata to upload.
     * @return The uploaded file metadata.
     */
    FileDTO uploadFile(FileDTO fileDTO);

    /**
     * Retrieves a file by its ID.
     *
     * @param fileId The ID of the file to retrieve.
     * @return The file metadata.
     */
    FileDTO getFileById(String fileId);

    /**
     * Deletes a file by its ID.
     *
     * @param fileId The ID of the file to delete.
     */
    void deleteFile(String fileId);

}
