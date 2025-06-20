package com.oide.user.dto;

import com.oide.user.entity.User;

import lombok.Data;

@Data
public class UserDTO {
    
    private Long userId;
    private String name;
    private String username;
    private String email;
    private String password;
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
