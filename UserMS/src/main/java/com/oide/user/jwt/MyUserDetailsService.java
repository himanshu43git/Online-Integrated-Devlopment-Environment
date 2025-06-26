package com.oide.user.jwt;

import com.oide.user.dto.UserDTO;
import com.oide.user.entity.User;
import com.oide.user.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {


        try{
            User dto = userService.getUserByEmail(email);
            return new CustomUserDetails(dto.getUserId(), dto.getUsername(), dto.getEmail(), dto.getPassword(), dto.getName(), null);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

}