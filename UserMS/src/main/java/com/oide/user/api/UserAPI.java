package com.oide.user.api;

import com.oide.user.dto.LoginDTO;
import com.oide.user.dto.ResponseDTO;
import com.oide.user.dto.UserDTO;
import com.oide.user.exceptions.CustomException;
import com.oide.user.jwt.CustomUserDetails;
import com.oide.user.jwt.JwtUtil;
import com.oide.user.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Validated
public class UserAPI {

    private UserService userService;

    private UserDetailsService userDetailsService;

    private AuthenticationManager authenticationManager;

    private JwtUtil jwtUtil;

    public UserAPI(UserService userService,
                   AuthenticationManager authenticationManager,
                   UserDetailsService userDetailsService,
                   JwtUtil jwtUtil)
    {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/signup")
    public ResponseEntity<ResponseDTO> registerUser(@RequestBody @Valid UserDTO userDTO){

        try {
            userService.createUser(userDTO);
            ResponseDTO response = new ResponseDTO("User created successfully", userDTO);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            ResponseDTO response = new ResponseDTO("Error creating user: " + e.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody LoginDTO loginDTO) throws CustomException {
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));
        }
        catch(AuthenticationException e){
            throw new CustomException("INVALID_CREDENTIALS");
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(loginDTO.getEmail());
        final String jwt = jwtUtil.generateToken(userDetails);

        return new ResponseEntity<>(jwt, HttpStatus.OK);
    }

}

