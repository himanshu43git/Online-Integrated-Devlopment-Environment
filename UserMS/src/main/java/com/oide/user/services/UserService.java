package com.oide.user.services;

import com.oide.user.dto.UserDTO;
import com.oide.user.entity.User;
import com.oide.user.exceptions.CustomException;

public interface UserService {
    
    public void createUser(UserDTO userDTO) throws CustomException;

    public UserDTO loginUser(UserDTO userDTO) throws CustomException;

    public User getUserById(Long userId);

    public void updateUser(String email, UserDTO userDTO);

    public User deleteUser(Long userId);

    public User getUserByUsername(String username) throws CustomException;

    public User getUserByEmail(String email) throws CustomException;
}
