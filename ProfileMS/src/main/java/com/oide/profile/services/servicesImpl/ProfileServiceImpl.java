package com.oide.profile.services.servicesImpl;

import com.oide.profile.dto.ProfileDTO;
import com.oide.profile.entity.Profile;
import com.oide.profile.entity.UserFile;
import com.oide.profile.exceptions.ProfileNotFoundException;
import com.oide.profile.exceptions.CustomException;
import com.oide.profile.exceptions.UserIdRelatesExceptions;
import com.oide.profile.repositories.FileRepository;
import com.oide.profile.repositories.ProfileRepository;
import com.oide.profile.services.ProfileService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository repository;

    private final FileRepository fileRepo;

    public ProfileServiceImpl(ProfileRepository repository, FileRepository fileRepo) {
        this.repository = repository;
        this.fileRepo = fileRepo;
    }

    /**
     * Create a new profile for a user. Expects profileDTO.userId to be non-null &
     * positive.
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
        Profile profile = repository.findById(userId)
                .orElseThrow(() -> new ProfileNotFoundException(userId));

        boolean exists = profile.getFileIds().stream()
                .anyMatch(f -> f.getFileId().equals(fileId));
        if (!exists) {
            UserFile uf = new UserFile();
            uf.setFileId(fileId);
            uf.setProfile(profile);
            profile.getFileIds().add(uf);
            repository.save(profile);
        }

        return userId;
    }

    /**
     * @param userId
     * @return
     */

    @Override
    public List<UserFile> getAllFileId(Long userId) {
        if (userId == null) {
            throw new UserIdRelatesExceptions("USER ID CANNOT BE NULL");
        }
        if (userId <= 0) {
            throw new UserIdRelatesExceptions("USER ID MUST BE > 0");
        }

        List<String> ids = fileRepo.findFileIdsByUserId(userId)
                .orElseThrow(() ->
                        new CustomException("NO PROFILE FOUND FOR USERID: " + userId))
                .stream()
                .map(String::toUpperCase)
                .collect(Collectors.toList());

        if (ids.isEmpty()) {
            throw new CustomException("NO FILES ARE RELATED FOR USERID: " + userId);
        }

        // 2) get a Profile proxy (no extra query yet)
        Profile profileRef = repository.getReferenceById(userId);

        // 3) map into UserFile
        return ids.stream()
                .map(fileIdStr -> {
                    UserFile uf = new UserFile();
                    uf.setFileId(fileIdStr);
                    uf.setProfile(profileRef);
                    return uf;
                })
                .collect(Collectors.toList());
    }

}
