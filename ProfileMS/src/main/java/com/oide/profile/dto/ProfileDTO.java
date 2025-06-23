package com.oide.profile.dto;

import com.oide.profile.entity.Profile;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

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
//    @Positive(message = "userId must be a positive number")
    private Long userId;

//    @Size(max = 100, message = "Name cannot exceed 100 characters")
    private String name;

//    @NotBlank(message = "Username is required")
//    @Size(max = 50, message = "Username cannot exceed 50 characters")
    private String username; 

//    @NotBlank(message = "Email is mandatory")
//    @Email(message = "Email must be valid")
//    @Size(max = 100, message = "Email cannot exceed 100 characters")
    private String email;

//    @NotBlank(message = "Password field cannot be empty")
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).{8,15}$",
            message = "Password should contain at least 1 digit, 1 lowercase, 1 uppercase, 1 special character and be 8-15 characters long"
    )
    private String password;

//    @Size(max = 500, message = "Bio cannot exceed 500 characters")
    private String bio;

//    @Size(max = 255, message = "Avatar URL cannot exceed 255 characters")
//    @URL(message = "avatarUrl must be a valid URL")
    private String avatarUrl;

//    @Size(max = 100, message = "Location cannot exceed 100 characters")
    private String location;

//    @Size(max = 255, message = "Personal URL cannot exceed 255 characters")
    @URL(message = "personalUrl must be a valid URL")
    private String personalUrl;


    // ----------- Mapping Methods -----------

    /**
     * Convert this DTO into a Profile entity.
     * Service layer should encode/hash the password (e.g., passwordEncoder.encode(...))
     * before or after calling this method.
     */
    public Profile toEntity() {
        Profile entity = new Profile();
        // Set primary key (userId) if provided. In create flow, controller/service sets this from path or after user creation.
        entity.setUserId(this.userId);

        entity.setName(this.name);
        entity.setUsername(this.username);
        entity.setEmail(this.email);
        entity.setPassword(this.password);
        entity.setBio(this.bio);
        entity.setAvatarUrl(this.avatarUrl);
        entity.setLocation(this.location);
        entity.setPersonalUrl(this.personalUrl);

        // lastActiveAt, createdAt, updatedAt handled by entity lifecycle callbacks
        return entity;
    }

    /**
     * Convert a Profile entity into this DTO.
     * Password field is set to null to avoid exposing it.
     */
    // public static ProfileDTO fromEntity(Profile entity) {
    //     ProfileDTO dto = new ProfileDTO();
    //     dto.setUserId(entity.getUserId());
    //     dto.setName(entity.getName());
    //     dto.setUsername(entity.getUsername());
    //     dto.setEmail(entity.getEmail());
    //     dto.setPassword(null); // never expose stored password
    //     dto.setBio(entity.getBio());
    //     dto.setAvatarUrl(entity.getAvatarUrl());
    //     dto.setLocation(entity.getLocation());
    //     dto.setPersonalUrl(entity.getPersonalUrl());
    //     return dto;
    // }
}
