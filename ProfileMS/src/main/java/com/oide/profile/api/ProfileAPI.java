package com.oide.profile.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.oide.profile.dto.ProfileDTO;
import com.oide.profile.exceptions.CustomException;
import com.oide.profile.services.ProfileService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

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
        Long createdUserId = profileService.createProfile(profileDTO);
        return new ResponseEntity<>(createdUserId, HttpStatus.CREATED);
    }

    @GetMapping("/get/{userId}")
    public ResponseEntity<ProfileDTO> getProfile(
            @PathVariable @Positive(message = "userId must be positive") Long userId) {
        ProfileDTO profile = profileService.getProfileByUserId(userId);
        return ResponseEntity.ok(profile);
    }

    /**
     * Add a fileId to the user’s profile.
     *
     * Example: POST /profile/42/files/613e1a2b3c4d5e6f7g8h9i0j
     */
    @PostMapping("/{userId}/files/{fileId}")
    public ResponseEntity<Void> addFileIdToProfile(
            @PathVariable @Positive(message = "userId must be positive") Long userId,
            @PathVariable String fileId) {
        profileService.addFileIdToProfile(userId, fileId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
