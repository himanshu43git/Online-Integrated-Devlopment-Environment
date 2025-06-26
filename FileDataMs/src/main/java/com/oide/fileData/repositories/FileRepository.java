package com.oide.fileData.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.oide.fileData.entities.FileEntity;

@Repository
public interface FileRepository extends MongoRepository<FileEntity, String> {

    /**
     * Custom query to find a file by its storage filename.
     *
     * @param storageFilename The storage filename to search for.
     * @return The FileEntity if found, otherwise null.
     */
    FileEntity findByStorageFilename(String storageFilename);

    /**
     * Custom query to delete a file by its storage filename.
     *
     * @param storageFilename The storage filename to delete.
     */
    void deleteByStorageFilename(String storageFilename);
    
}
