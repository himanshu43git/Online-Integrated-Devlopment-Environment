package com.oide.profile.entity;

import com.oide.profile.dto.ProfileDTO;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "profiles")
public class Profile {

    /**
     * Use the userId from User microservice as the primary key.
     */
    @Id
    @Column(nullable = false, updatable = false)
    private Long userId;

    // Display / personal info
    @Column(length = 100)
    private String name;

    @Column(length = 50)
    private String username;

    @Column(length = 100)
    private String email;

    /**
     * Stored as encoded/hashed in service layer before saving.
     */
    @Column(length = 255)
    private String password;

    @Column(length = 500)
    private String bio;

    @Column(length = 255)
    private String avatarUrl;

    @Column(length = 100)
    private String location;

    @Column(length = 255)
    private String personalUrl;

    // Activity metadata
    private LocalDateTime lastActiveAt;

    // Timestamps
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @ElementCollection
    @CollectionTable(
            name = "profile_file_ids",
            joinColumns = @JoinColumn(name = "user_id")
    )
    @Column(name = "file_id", length = 40)
    private List<String> fileIds = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
        if (lastActiveAt == null) {
            lastActiveAt = now;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    /**
     * Map this entity to ProfileDTO for use in controllers/services.
     */
    public ProfileDTO toDTO() {
        ProfileDTO dto = new ProfileDTO();
        dto.setUserId(this.userId);
        dto.setName(this.name);
        dto.setUsername(this.username);
        dto.setEmail(this.email);
        dto.setPassword(null); // never expose the hashed password
        dto.setBio(this.bio);
        dto.setAvatarUrl(this.avatarUrl);
        dto.setLocation(this.location);
        dto.setPersonalUrl(this.personalUrl);
        dto.setFileIds(this.fileIds);
        return dto;
    }
}
