package com.oide.profile.dto;

import com.oide.profile.entity.Profile;
import com.oide.profile.entity.UserFile;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.validator.constraints.URL;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileDTO {

    private Long userId;

    private String name;
    private String username;
    private String email;

    @Pattern(
        regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).{8,15}$",
        message = "Password must contain upper, lower, digit, special char (8–15 chars)"
    )
    private String password;

    private String bio;
    private String avatarUrl;
    private String location;

    @URL(message = "personalUrl must be a valid URL")
    private String personalUrl;

    private List<String> fileIds = new ArrayList<>();

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

        List<UserFile> fileEntities = this.fileIds.stream().map(fid -> {
            UserFile uf = new UserFile();
            uf.setFileId(fid);
            uf.setProfile(entity);  // important: set back-reference
            return uf;
        }).collect(Collectors.toList());

        entity.setFileIds(fileEntities);
        return entity;
    }

    public static ProfileDTO fromEntity(Profile entity) {
        return entity.toDTO();  // already mapped in Profile
    }
}
