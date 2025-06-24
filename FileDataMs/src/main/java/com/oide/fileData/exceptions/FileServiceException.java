package com.oide.fileData.exceptions;

public class FileServiceException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;

    public FileServiceException(String message) {
        super(message);
    }

    public FileServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileServiceException(Throwable cause) {
        super(cause);
    }

}
