package com.oide.profile.exceptions;

public class CustomException extends RuntimeException{

    private String message;

    public CustomException(String message){
        super(message);
    }
}
