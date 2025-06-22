package com.oide.profile.exceptions;

public class CustomException extends RuntimeException{

    @java.io.Serial
//    static final long serialVersionUID = -7034897190745766939L;

    private String message;

    public CustomException(String message){
        super(message);
    }
}
