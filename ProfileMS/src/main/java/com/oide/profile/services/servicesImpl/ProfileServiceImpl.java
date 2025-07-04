package com.oide.profile.services.servicesImpl;

import com.oide.profile.dto.ProfileDTO;
import com.oide.profile.entity.Profile;
import com.oide.profile.exceptions.ProfileNotFoundException;
import com.oide.profile.exceptions.CustomException;
import com.oide.profile.repositories.ProfileRepository;
import com.oide.profile.services.ProfileService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository repository;

    public ProfileServiceImpl(ProfileRepository repository) {
        this.repository = repository;
    }

    /**
     * Create a new profile for a user. Expects profileDTO.userId to be non-null & positive.
     */
    @Override
    @Transactional
    public Long createProfile(ProfileDTO profileDTO) {
        Long userId = profileDTO.getUserId();
        if (userId == null || userId <= 0) {
            throw new CustomException("userId must be provided and positive");
        }
        if (repository.existsById(userId)) {
            throw new CustomException("Profile already exists for userId: " + userId);
        }
        if (!StringUtils.hasText(profileDTO.getPassword())) {
            throw new CustomException("Password must not be empty");
        }

        // Map DTO → Entity (fileIds list will be empty by default)
        Profile entity = profileDTO.toEntity();
        entity.setUserId(userId);
        // TODO: encode the password before setting if you introduce a PasswordEncoder
        entity.setPassword(profileDTO.getPassword());

        repository.save(entity);
        return userId;
    }

    /**
     * Retrieve an existing profile by userId.
     */
    @Override
    @Transactional(readOnly = true)
    public ProfileDTO getProfileByUserId(Long userId) {
        if (userId == null || userId <= 0) {
            throw new CustomException("userId must be provided and positive");
        }
        Profile profile = repository.findById(userId)
                .orElseThrow(() -> new ProfileNotFoundException(userId));
        return profile.toDTO();
    }

    /**
     * Update fields in an existing profile. Only non-null DTO fields overwrite.
     */
    @Override
    @Transactional
    public ProfileDTO updateProfile(Long userId, ProfileDTO profileDTO) {
        if (userId == null || userId <= 0) {
            throw new CustomException("userId must be provided and positive");
        }
        Profile existing = repository.findById(userId)
                .orElseThrow(() -> new ProfileNotFoundException(userId));

        // Conditionally overwrite
        Optional.ofNullable(profileDTO.getName()).ifPresent(existing::setName);
        Optional.ofNullable(profileDTO.getUsername()).ifPresent(existing::setUsername);
        Optional.ofNullable(profileDTO.getEmail()).ifPresent(existing::setEmail);
        if (StringUtils.hasText(profileDTO.getPassword())) {
            existing.setPassword(profileDTO.getPassword());
        }
        Optional.ofNullable(profileDTO.getBio()).ifPresent(existing::setBio);
        Optional.ofNullable(profileDTO.getAvatarUrl()).ifPresent(existing::setAvatarUrl);
        Optional.ofNullable(profileDTO.getLocation()).ifPresent(existing::setLocation);
        Optional.ofNullable(profileDTO.getPersonalUrl()).ifPresent(existing::setPersonalUrl);

        Profile updated = repository.save(existing);
        return updated.toDTO();
    }

    /**
     * Delete a profile by userId.
     */
    @Override
    @Transactional
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
     * Check whether a profile exists.
     */
    @Override
    @Transactional(readOnly = true)
    public boolean existsByUserId(Long userId) {
        if (userId == null || userId <= 0) {
            throw new CustomException("userId must be provided and positive");
        }
        return repository.existsById(userId);
    }

    /**
     * Add a fileId to the user’s profile (no-ops if already present).
     */
    @Override
    @Transactional
    public Long addFileIdToProfile(Long userId, String fileId) {
        if (userId == null || userId <= 0) {
            throw new CustomException("userId must be provided and positive");
        }
        if (!StringUtils.hasText(fileId)) {
            throw new CustomException("fileId must not be empty");
        }

        Profile profile = repository.findById(userId)
                .orElseThrow(() -> new ProfileNotFoundException(userId));

        if (!profile.getFileIds().contains(fileId)) {
            profile.getFileIds().add(fileId);
            repository.save(profile);
        }

        return userId;
    }


}
