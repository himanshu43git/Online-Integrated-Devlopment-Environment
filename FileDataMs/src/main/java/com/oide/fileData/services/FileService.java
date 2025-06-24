package com.oide.fileData.services;

public interface FileService {
    
    /**
     * Uploads a file to the server.
     *
     * @param filePath the path of the file to be uploaded
     * @param userId the ID of the user uploading the file
     * @return a message indicating the success or failure of the upload
     */
    String uploadFile(String filePath, Long userId);

    /**
     * Downloads a file from the server.
     *
     * @param fileId the ID of the file to be downloaded
     * @return a message indicating the success or failure of the download
     */
    String getFile(Long fileId);

    

}
