package com.oide.user.entity;

import com.oide.user.dto.UserDTO;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users", 
       uniqueConstraints = {
         @UniqueConstraint(columnNames = "email"),
         @UniqueConstraint(columnNames = "username")
       })
@Data
@NoArgsConstructor
public class User {
    
    @Id
    private Long userId;
    private String name;
    private String username;
    private String email;
    private String password;
    private Long profileId;

    public UserDTO toDTO(){
        UserDTO dto = new UserDTO();
        dto.setUserId(this.userId);
        dto.setName(this.name);
        dto.setUsername(this.username);
        dto.setEmail(this.email);
        dto.setPassword(this.password);
        dto.setProfileId(this.profileId);

        return dto;
    }
}
