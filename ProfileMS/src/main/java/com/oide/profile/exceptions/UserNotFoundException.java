package com.oide.profile.exceptions;

/**
 * Thrown when a referenced User ID does not exist in the User microservice.
 */
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long userId) {
        super("User not found for userId: " + userId);
    }
}
