package com.oide.profile.services.servicesImpl;

import com.oide.profile.dto.ProfileDTO;
import com.oide.profile.entity.Profile;
import com.oide.profile.exceptions.ProfileNotFoundException;
import com.oide.profile.exceptions.CustomException; // ensure you have/import CustomException
import com.oide.profile.repositories.ProfileRepository;
import com.oide.profile.services.ProfileService;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository repository;
    // If you later verify user existence, inject a UserClient or similar.
    // private final UserClient userClient;

    public ProfileServiceImpl(ProfileRepository repository
            /*, UserClient userClient */) {
        this.repository = repository;
        // this.userClient = userClient;
    }

    /**
     * Create a new profile for a user.
     * Expects profileDTO.userId to be non-null and positive.
     */
    @Override
    public Long createProfile(ProfileDTO profileDTO) {
//        Long userId = profileDTO.getUserId();
//        // Validate userId presence
//        if (userId == null || userId <= 0) {
//            throw new CustomException("userId must be provided and positive");
//        }
//        // Check that profile does not already exist
//        if (repository.existsById(userId)) {
//            throw new CustomException("Profile already exists for userId: " + userId);
//        }
//        // Validate password non-empty
//        if (!StringUtils.hasText(profileDTO.getPassword())) {
//            throw new CustomException("Password must not be empty");
//        }

        Optional<Profile> exsisting = repository.findByUsername(profileDTO.getUsername());

        if(exsisting.isPresent()){
            throw new CustomException("USER_ALREADY_EXIST");
        }
        // Map DTO to entity
        Profile entity = profileDTO.toEntity();
        // Set the provided userId as primary key
        Long id = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
        entity.setUserId(id);
        // Store raw password as-is (no encoding here)
        entity.setPassword(profileDTO.getPassword());
        // Save entity
        Profile saved = repository.save(entity);
        // Return the userId of the saved profile
        return saved.getUserId();
    }

    /**
     * Retrieve an existing profile by userId.
     */
    @Override
    public ProfileDTO getProfileByUserId(Long userId) {
        if (userId == null || userId <= 0) {
            throw new CustomException("userId must be provided and positive");
        }
        Optional<Profile> profileExist = repository.findById(userId);
        if (profileExist.isPresent()) {
            return profileExist.get().toDTO();
        } else {
            throw new ProfileNotFoundException(userId);
        }
    }

    /**
     * Update an existing profile.
     * Fields in profileDTO that are non-null overwrite existing values.
     * Raw password is stored as-is if provided.
     */
    @Override
    public ProfileDTO updateProfile(Long userId, ProfileDTO profileDTO) {
        if (userId == null || userId <= 0) {
            throw new CustomException("userId must be provided and positive");
        }
        Profile existing = repository.findById(userId)
                .orElseThrow(() -> new ProfileNotFoundException(userId));

        // Update fields if provided (non-null)
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
        // lastActiveAt, createdAt, updatedAt handled by entity lifecycle callbacks

        Profile updated = repository.save(existing);
        return updated.toDTO();
    }

    /**
     * Delete a profile by userId.
     */
    @Override
    public void deleteProfileByUserId(Long userId) {
        if (userId == null || userId <= 0) {
            throw new CustomException("userId must be provided and positive");
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
            throw new CustomException("userId must be provided and positive");
        }
        return repository.existsById(userId);
    }
}
