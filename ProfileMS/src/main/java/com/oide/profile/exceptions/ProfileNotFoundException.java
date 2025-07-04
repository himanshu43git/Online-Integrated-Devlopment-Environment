package com.oide.profile.exceptions;

/**
 * Thrown when a Profile for a given userId is not found.
 */
public class ProfileNotFoundException extends RuntimeException {
    public ProfileNotFoundException(Long userId) {
        super("PROFILE_NOT_FOUND_FOR_USERID : " + userId);
    }
}
