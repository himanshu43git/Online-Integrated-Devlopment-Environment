package com.oide.user.dto;

import com.oide.user.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    
    @Positive(message = "userId must be a positive number")
    private Long userId;

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    private String name;

    @NotBlank(message = "Username is required")
    @Size(max = 50, message = "Username cannot exceed 50 characters")
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Size(max = 100, message = "Email cannot exceed 100 characters")
    private String email;

    @NotBlank(message = "Password is required")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).{8,15}$", message = "Password should contain atleast 1 digit, 1 lowercase, 1 uppercase, 1 special character and minimum 8 characters")
    private String password;

    @Positive(message = "profileId must be a positive number")
    private Long profileId;

    public User toEntity(){
        User user = new User();
        user.setUserId(this.userId);
        user.setName(this.name);
        user.setUsername(this.username);
        user.setEmail(this.email);
        user.setPassword(this.password);
        user.setProfileId(this.profileId);
        return user;
    }
}
