package com.oide.profile.dto;

import com.oide.profile.entity.Profile;
// import com.oide.profile.entity.UserFiles;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

import java.util.List;
import java.util.ArrayList;

/**
 * DTO for Profile create/update.
 * Validation annotations enforce constraints on incoming data.
 * Mapping methods convert to/from Profile entity.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileDTO {

    /**
     * For create: may be null.
     * For update: set from path or caller, must be positive if provided.
     */
    private Long userId;

    private String name;

    private String username; 

    private String email;

    @Pattern(
        regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).{8,15}$",
        message = "Password should contain at least 1 digit, 1 lowercase, 1 uppercase, 1 special character and be 8-15 characters long"
    )
    private String password;

    private String bio;

    private String avatarUrl;

    private String location;

    @URL(message = "personalUrl must be a valid URL")
    private String personalUrl;

    /**
     * All the file IDs this user has uploaded.
     */
    private List<String> fileIds = new ArrayList<>();

    // ----------- Mapping Methods -----------

    /**
     * Convert this DTO into a Profile entity.
     * Sets all fields; lifecycle callbacks handle timestamps.
     */
    public Profile toEntity() {
        Profile entity = new Profile();
        entity.setUserId(this.userId);
        entity.setName(this.name);
        entity.setUsername(this.username);
        entity.setEmail(this.email);
        entity.setPassword(this.password);
        entity.setBio(this.bio);
        entity.setAvatarUrl(this.avatarUrl);
        entity.setLocation(this.location);
        entity.setPersonalUrl(this.personalUrl);
         entity.setFileIds(this.fileIds);
        return entity;
    }

    /**
     * Populate this DTO from a Profile entity.
     * Password is omitted for safety.
     */
    public static ProfileDTO fromEntity(Profile entity) {
        ProfileDTO dto = new ProfileDTO();
        dto.setUserId(entity.getUserId());
        dto.setName(entity.getName());
        dto.setUsername(entity.getUsername());
        dto.setEmail(entity.getEmail());
        dto.setPassword(null);
        dto.setBio(entity.getBio());
        dto.setAvatarUrl(entity.getAvatarUrl());
        dto.setLocation(entity.getLocation());
        dto.setPersonalUrl(entity.getPersonalUrl());
         dto.setFileIds(entity.getFileIds());
        return dto;
    }
}
