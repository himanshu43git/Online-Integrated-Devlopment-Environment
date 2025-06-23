package com.oide.profile.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oide.profile.dto.ProfileDTO;
import com.oide.profile.exceptions.CustomException;
import com.oide.profile.services.ProfileService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/profile")
@Validated
@CrossOrigin
public class ProfileAPI {

    private final ProfileService profileService;

    public ProfileAPI(ProfileService profileService) {
        this.profileService = profileService;
    }

    @PostMapping("/addProfile")
    public ResponseEntity<Long> addProfile(@Valid @RequestBody ProfileDTO profileDTO) throws CustomException {
        // profileDTO.userId must be provided and positive (enforced by @Positive if non-null, and we check non-null in service)
        Long createdUserId = profileService.createProfile(profileDTO);
        return new ResponseEntity<>(createdUserId, HttpStatus.CREATED);
    }

    @GetMapping("/profile/get/{userId}")
    public ResponseEntity<ProfileDTO> getProfile(@PathVariable Long userId) {
        // Validate userId
        if (userId == null || userId <= 0) {
            throw new CustomException("Invalid userId");
        }
        ProfileDTO profile = profileService.getProfileByUserId(userId);
        return ResponseEntity.ok(profile);
    }
}
