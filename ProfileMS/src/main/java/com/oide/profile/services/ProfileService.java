package com.oide.profile.services;

import com.oide.profile.dto.ProfileDTO;

/**
 * Service interface for Profile operations.
 * Implement this interface to provide business logic for creating, retrieving,
 * updating, and deleting profiles.
 */
public interface ProfileService {

    /**
     * Create a new profile for a user.
     * @param profileDTO DTO containing profile data; profileDTO.getUserId() must be non-null and positive.
     *                   Password in the DTO should be raw; implementation must encode/hash it before saving.
     * @return The created ProfileDTO (with fields as stored, e.g., hashed password not exposed).
     * @throws IllegalArgumentException if profileDTO.userId is null or not positive, or if a profile already exists.
     * @throws RuntimeException or custom exceptions (e.g., UserNotFoundException) if the referenced user does not exist.
     */
    ProfileDTO createProfile(ProfileDTO profileDTO);

    /**
     * Retrieve an existing profile by userId.
     * @param userId ID of the user whose profile is requested; must be non-null and positive.
     * @return The ProfileDTO for the given userId.
     * @throws IllegalArgumentException if userId is null or not positive.
     * @throws RuntimeException or custom exceptions (e.g., ProfileNotFoundException) if no profile exists.
     */
    ProfileDTO getProfileByUserId(Long userId);

    /**
     * Update an existing profile.
     * @param userId ID of the user whose profile is to be updated; must be non-null and positive.
     * @param profileDTO DTO containing updated fields. Fields that are null in DTO can be ignored or cleared
     *                   based on your implementation policy. Password, if non-null, should be raw and will be encoded.
     * @return The updated ProfileDTO.
     * @throws IllegalArgumentException if userId is null or not positive.
     * @throws RuntimeException or custom exceptions (e.g., ProfileNotFoundException) if no profile exists.
     */
    ProfileDTO updateProfile(Long userId, ProfileDTO profileDTO);

    /**
     * Delete a profile by userId.
     * @param userId ID of the user whose profile is to be deleted; must be non-null and positive.
     * @throws IllegalArgumentException if userId is null or not positive.
     * @throws RuntimeException or custom exceptions (e.g., ProfileNotFoundException) if no profile exists.
     */
    void deleteProfileByUserId(Long userId);

    /**
     * Check whether a profile exists for the given userId.
     * @param userId ID of the user to check; must be non-null and positive.
     * @return true if a profile exists, false otherwise.
     * @throws IllegalArgumentException if userId is null or not positive.
     */
    boolean existsByUserId(Long userId);
}
