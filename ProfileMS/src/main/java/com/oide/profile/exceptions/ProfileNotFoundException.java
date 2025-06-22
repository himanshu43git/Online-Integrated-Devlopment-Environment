package com.oide.profile.exceptions;

/**
 * Thrown when a Profile for a given userId is not found.
 */
public class ProfileNotFoundException extends RuntimeException {
    public ProfileNotFoundException(Long userId) {
        super("Profile not found for userId: " + userId);
    }
}
