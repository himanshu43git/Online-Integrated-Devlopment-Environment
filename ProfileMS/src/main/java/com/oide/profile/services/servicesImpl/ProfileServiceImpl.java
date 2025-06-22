package com.oide.profile.services.servicesImpl;

import com.oide.profile.dto.ProfileDTO;
import com.oide.profile.entity.Profile;
import com.oide.profile.exceptions.ProfileNotFoundException;
import com.oide.profile.exceptions.UserNotFoundException;
import com.oide.profile.repositories.ProfileRepository;
import com.oide.profile.services.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository repository;
    // If you later verify user existence, inject a UserClient or similar.
    // private final UserClient userClient;

    @Autowired
    public ProfileServiceImpl(ProfileRepository repository
            /*, UserClient userClient */) {
        this.repository = repository;
        // this.userClient = userClient;
    }

    /**
     * Create a new profile for a user.
     * No password encoding: raw password from DTO is stored as-is.
     */
    @Override
    public ProfileDTO createProfile(ProfileDTO profileDTO) {
        Long userId = profileDTO.getUserId();
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("userId must be provided and positive");
        }
        if (repository.existsById(userId)) {
            throw new IllegalArgumentException("Profile already exists for userId: " + userId);
        }
        // TODO: optionally verify user exists in User microservice:
        // boolean userExists = userClient.existsById(userId);
        // if (!userExists) {
        //     throw new UserNotFoundException(userId);
        // }

        // Map DTO to entity
        Profile entity = profileDTO.toEntity();
        // Directly store raw password (no encoding)
        if (!StringUtils.hasText(profileDTO.getPassword())) {
            throw new IllegalArgumentException("Password must not be empty");
        }
        entity.setPassword(profileDTO.getPassword());

        // Ensure primary key set
        entity.setUserId(userId);

        Profile saved = repository.save(entity);
        return ProfileDTO.fromEntity(saved);
    }

    /**
     * Retrieve an existing profile by userId.
     */
    @Override
    public ProfileDTO getProfileByUserId(Long userId) {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("userId must be provided and positive");
        }
        Profile entity = repository.findById(userId)
                .orElseThrow(() -> new ProfileNotFoundException(userId));
        return ProfileDTO.fromEntity(entity);
    }

    /**
     * Update an existing profile.
     * Fields in profileDTO that are non-null overwrite existing values.
     * Raw password is stored as-is if provided.
     */
    @Override
    public ProfileDTO updateProfile(Long userId, ProfileDTO profileDTO) {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("userId must be provided and positive");
        }
        Profile existing = repository.findById(userId)
                .orElseThrow(() -> new ProfileNotFoundException(userId));

        // Update fields if provided (non-null).
        if (profileDTO.getName() != null) {
            existing.setName(profileDTO.getName());
        }
        if (profileDTO.getUsername() != null) {
            existing.setUsername(profileDTO.getUsername());
        }
        if (profileDTO.getEmail() != null) {
            existing.setEmail(profileDTO.getEmail());
        }
        String newPassword = profileDTO.getPassword();
        if (StringUtils.hasText(newPassword)) {
            // Directly store raw password
            existing.setPassword(newPassword);
        }
        if (profileDTO.getBio() != null) {
            existing.setBio(profileDTO.getBio());
        }
        if (profileDTO.getAvatarUrl() != null) {
            existing.setAvatarUrl(profileDTO.getAvatarUrl());
        }
        if (profileDTO.getLocation() != null) {
            existing.setLocation(profileDTO.getLocation());
        }
        if (profileDTO.getPersonalUrl() != null) {
            existing.setPersonalUrl(profileDTO.getPersonalUrl());
        }
        // lastActiveAt is typically updated on actual user activity elsewhere

        Profile updated = repository.save(existing);
        return ProfileDTO.fromEntity(updated);
    }

    /**
     * Delete a profile by userId.
     */
    @Override
    public void deleteProfileByUserId(Long userId) {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("userId must be provided and positive");
        }
        if (!repository.existsById(userId)) {
            throw new ProfileNotFoundException(userId);
        }
        repository.deleteById(userId);
    }

    /**
     * Check whether a profile exists for the given userId.
     */
    @Override
    public boolean existsByUserId(Long userId) {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("userId must be provided and positive");
        }
        return repository.existsById(userId);
    }
}
