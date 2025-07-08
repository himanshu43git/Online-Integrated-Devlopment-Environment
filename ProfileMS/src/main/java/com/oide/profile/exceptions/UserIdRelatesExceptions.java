package com.oide.profile.exceptions;

import java.io.Serial;

public class UserIdRelatesExceptions extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    private String message;

    // constructor will be called when the userID is null
    public UserIdRelatesExceptions(){
        super("USER ID CANNOT BE NULL");
    }

    // CONSTRUCTOR WILL BE CALLED FOR ONE ARGUMENT

    public UserIdRelatesExceptions(String message){
        super(message);
    }
}
