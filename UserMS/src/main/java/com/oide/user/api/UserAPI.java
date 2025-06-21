package com.oide.user.api;

import com.oide.user.dto.ResponseDTO;
import com.oide.user.dto.UserDTO;
import com.oide.user.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@Validated
public class UserAPI {

    private UserService userService;

    public UserAPI(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<ResponseDTO> registerUser(@RequestBody @Valid UserDTO userDTO){

        ResponseDTO response = new ResponseDTO();
        try {
            userService.createUser(userDTO);
            response.setMessage("User registered successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setMessage("Error registering user: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }

    }

}

