package com.oide.fileData.service.serviceImpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.oide.fileData.dto.FileDTO;
import com.oide.fileData.entities.FileEntity;
import com.oide.fileData.repositories.FileRepository;
import com.oide.fileData.service.FileService;

import jakarta.annotation.PostConstruct;

@Service
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;

    @Value("${files.save}")
    private String rootDir;

    public FileServiceImpl(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @PostConstruct
    public void init() {
        Path dirPath = Paths.get(rootDir);
        if (Files.notExists(dirPath)) {
            try {
                Files.createDirectories(dirPath);
                System.out.println("Directory created: " + dirPath);
            } catch (IOException e) {
                throw new RuntimeException("Could not initialize storage directory: " + dirPath, e);
            }
        } else {
            System.out.println("Directory already exists: " + dirPath);
        }
    }

    @Override
    public FileDTO uploadFile(FileDTO fileDTO) {
        // 1) generate a unique fileId and a unique storage filename
        String fileId = UUID.randomUUID().toString();
        String original = fileDTO.getOriginalFilename();
        long ts = Instant.now().toEpochMilli();
        String storageFilename = fileDTO.getUserId() + "_" + ts + "_" + original;

        // 2) decode Base64 and write the bytes to disk
        byte[] data = Base64.getDecoder().decode(fileDTO.getFileContent());
        Path target = Paths.get(rootDir).resolve(storageFilename).normalize();
        try {
            Files.write(target, data);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write file to disk: " + target, e);
        }

        // 3) if a file with this storageFilename already exists, just return it
        FileEntity existing = fileRepository.findByStorageFilename(storageFilename);
        if (existing != null) {
            return FileDTO.fromEntity(existing);
        }

        // 4) build the Mongo entity — include the generated fileId
        FileEntity entity = new FileEntity(
                fileDTO.getUserId(),
                original,
                storageFilename,
                Instant.ofEpochMilli(ts));
        entity.setId(fileId);
        // leave entity.fileContent null to keep DB slim

        // 5) save and return DTO
        FileEntity saved = fileRepository.save(entity);
        return FileDTO.fromEntity(saved);
    }

    @Override
    public FileDTO getFileById(String fileId) {
        Optional<FileEntity> opt = fileRepository.findById(fileId);
        if (!opt.isPresent()) {
            throw new RuntimeException("File not found: " + fileId);
        }
        FileEntity e = opt.get();

        // 1) read bytes from disk, re-encode as Base64
        Path path = Paths.get(rootDir).resolve(e.getStorageFilename()).normalize();
        byte[] bytes;
        try {
            bytes = Files.readAllBytes(path);
        } catch (IOException ex) {
            throw new RuntimeException("Could not read file: " + path, ex);
        }
        String b64 = Base64.getEncoder().encodeToString(bytes);

        // 2) build DTO
        FileDTO dto = FileDTO.fromEntity(e);
        dto.setFileContent(b64);
        return dto;
    }

    @Override
    public void deleteFile(String fileId) {
        Optional<FileEntity> opt = fileRepository.findById(fileId);
        if (!opt.isPresent()) {
            throw new RuntimeException("File not found: " + fileId);
        }
        FileEntity e = opt.get();

        // 1) delete from disk
        Path path = Paths.get(rootDir).resolve(e.getStorageFilename()).normalize();
        try {
            Files.deleteIfExists(path);
        } catch (IOException ex) {
            throw new RuntimeException("Failed to delete file: " + path, ex);
        }

        // 2) delete metadata record
        fileRepository.deleteById(fileId);
    }

    @Override
    public void updateFile(FileDTO fileDTO) {
        // 1) Validate input
        if (fileDTO.getFileId() == null || fileDTO.getFileId().isBlank()) {
            throw new IllegalArgumentException("fileId is required for update");
        }

        // 2) Load existing entity
        Optional<FileEntity> opt = fileRepository.findById(fileDTO.getFileId());
        if (!opt.isPresent()) {
            throw new RuntimeException("File not found: " + fileDTO.getFileId());
        }
        FileEntity entity = opt.get();

        // 3) Overwrite file on disk if new content was provided
        if (fileDTO.getFileContent() != null && !fileDTO.getFileContent().isBlank()) {
            byte[] data = Base64.getDecoder().decode(fileDTO.getFileContent());
            Path target = Paths.get(rootDir)
                    .resolve(entity.getStorageFilename())
                    .normalize();
            try {
                Files.write(target, data);
            } catch (IOException e) {
                throw new RuntimeException("Failed to write updated file to disk: " + target, e);
            }
        }

        // 4) Update metadata if original filename changed
        String newOriginal = fileDTO.getOriginalFilename();
        if (newOriginal != null
                && !newOriginal.isBlank()
                && !newOriginal.equals(entity.getOriginalFilename())) {
            entity.setOriginalFilename(newOriginal);
        }

        // 5) Refresh uploadTime
        entity.setUploadTime(Instant.now());

        // 6) Persist the updated entity
        fileRepository.save(entity);
    }

}
