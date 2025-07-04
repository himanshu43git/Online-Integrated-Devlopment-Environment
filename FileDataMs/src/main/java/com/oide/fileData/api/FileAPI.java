package com.oide.fileData.api;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.oide.fileData.dto.FileDTO;
import com.oide.fileData.service.FileService;

@RestController
@RequestMapping("/files")
public class FileAPI {

    private final FileService fileService;

    public FileAPI(FileService fileService) {
        this.fileService = fileService;
    }

    /**
     * Upload (save) a new file.
     * Expects FileDTO.userId, FileDTO.originalFilename & FileDTO.fileContent (Base64).
     * Returns 201 Created + saved metadata (fileId, storageFilename, uploadTime).
     */
    @PostMapping("/save")
    public ResponseEntity<FileDTO> saveFile(@Valid @RequestBody FileDTO fileDTO) {
        try {
            FileDTO saved = fileService.uploadFile(fileDTO);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(saved);
        } catch (Exception ex) {
            // any unexpected error → 500
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Could not store file: " + ex.getMessage(),
                    ex
            );
        }
    }

    /**
     * Fetch a previously uploaded file by its ID.
     * Returns 200 OK + FileDTO (with fileContent re-populated as Base64).
     */
    @GetMapping("/{id}")
    public ResponseEntity<FileDTO> getFile(@PathVariable String id) {
        System.out.println("Here in getFile with id: " + id);
        try {
            FileDTO dto = fileService.getFileById(id);
            return ResponseEntity.ok(dto);
        } catch (RuntimeException ex) {
            // service throws RuntimeException when not found
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    ex.getMessage(),
                    ex
            );
        }
    }

    /**
     * Delete a previously uploaded file (both disk + metadata).
     * Returns 204 No Content on success.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFile(@PathVariable String id) {
        try {
            fileService.deleteFile(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException ex) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    ex.getMessage(),
                    ex
            );
        }
    }
}
