package com.oide.profile.entity;

import com.oide.profile.dto.ProfileDTO;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "profiles")
public class Profile {

    @Id
    @Column(nullable = false, updatable = false)
    private Long userId;

    @Column(length = 100)
    private String name;

    @Column(length = 50)
    private String username;

    @Column(length = 100)
    private String email;

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

    private LocalDateTime lastActiveAt;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserFile> fileIds = new ArrayList<>();

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

    public ProfileDTO toDTO() {
        ProfileDTO dto = new ProfileDTO();
        dto.setUserId(this.userId);
        dto.setName(this.name);
        dto.setUsername(this.username);
        dto.setEmail(this.email);
        dto.setPassword(null);
        dto.setBio(this.bio);
        dto.setAvatarUrl(this.avatarUrl);
        dto.setLocation(this.location);
        dto.setPersonalUrl(this.personalUrl);
        dto.setFileIds(this.fileIds.stream().map(UserFile::getFileId).collect(Collectors.toList()));
        return dto;
    }
}
