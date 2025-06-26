package com.oide.user.jwt;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomUserDetails implements UserDetails{

    private Long id;
    private String username;
    private String email;
    private String password;
    private String name;
    public Collection<? extends GrantedAuthority> authorities;


}
