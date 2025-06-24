package com.oide.fileData.services.servicesImpl;

import com.oide.fileData.entities.FileEntity;
import com.oide.fileData.exceptions.FileServiceException;
import com.oide.fileData.repositories.FileRepository;
import com.oide.fileData.services.FileService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.*;
import java.time.Instant;
import java.util.Base64;

@Service
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;
    private final Path storageLocation;

    public FileServiceImpl(
            FileRepository fileRepository,
            @Value("${file.storage.location:uploads}") String storageDir
    ) {
        this.fileRepository = fileRepository;
        this.storageLocation = Paths.get(storageDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.storageLocation);
        } catch (IOException e) {
            throw new FileServiceException("Could not create storage directory: " + storageLocation, e);
        }
    }

    @Override
    public String uploadFile(String filePath, Long userId) {
        if (filePath == null || filePath.isBlank()) {
            throw new FileServiceException("Invalid file path");
        }
        if (userId == null || userId <= 0) {
            throw new FileServiceException("Invalid userId");
        }

        Path source = Paths.get(filePath);
        if (!Files.exists(source) || !Files.isRegularFile(source)) {
            throw new FileServiceException("File not found: " + filePath);
        }

        String originalName = source.getFileName().toString();
        String storageName = userId + "_" + System.currentTimeMillis() + "_" + originalName;
        Path target = storageLocation.resolve(storageName);

        try {
            Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new FileServiceException("Failed to store file: " + e.getMessage(), e);
        }

        FileEntity entity = new FileEntity();
        entity.setUserId(userId);
        entity.setOriginalFilename(originalName);
        entity.setStorageFilename(storageName);
        entity.setUploadTime(Instant.now());

        FileEntity saved = fileRepository.save(entity);
        return "Uploaded successfully. File ID: " + saved.getId();
    }

    @Override
    public String getFile(Long fileId) {
        if (fileId == null || fileId <= 0) {
            throw new FileServiceException("Invalid fileId");
        }

        FileEntity entity = fileRepository.findById(fileId)
                .orElseThrow(() -> new FileServiceException("File not found for id: " + fileId));

        Path fileOnDisk = storageLocation.resolve(entity.getStorageFilename()).normalize();
        if (!Files.exists(fileOnDisk) || !Files.isRegularFile(fileOnDisk)) {
            throw new FileServiceException("Stored file missing: " + entity.getStorageFilename());
        }

        try {
            byte[] data = Files.readAllBytes(fileOnDisk);
            // return Base64 so it can be transported as String
            return Base64.getEncoder().encodeToString(data);
        } catch (IOException e) {
            throw new FileServiceException("Failed to read file: " + e.getMessage(), e);
        }
    }
}
