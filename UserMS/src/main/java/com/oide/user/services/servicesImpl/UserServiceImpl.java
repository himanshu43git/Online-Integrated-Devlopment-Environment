package com.oide.user.services.servicesImpl;

import java.util.Optional;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.oide.user.dto.UserDTO;
import com.oide.user.entity.User;
import com.oide.user.exceptions.CustomException;
import com.oide.user.repositories.UserRepository;
import com.oide.user.services.UserService;

import jakarta.transaction.Transactional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void createUser(UserDTO userDTO) throws CustomException {
        Optional<User> existingUser = userRepository.findByEmail(userDTO.getEmail());

        if(existingUser.isPresent()){
            throw new CustomException("USER_ALREADY_EXISTS");
        }

        Long userId = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;

        User user = new User();
        user.setUserId(userId);
        user.setName(userDTO.getName());
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setProfileId(userDTO.getProfileId());  
        
        userRepository.save(user);
    }

    @Override
    public UserDTO loginUSer(UserDTO userDTO) throws CustomException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'loginUSer'");
    }

    @Override
    public User getUserById(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if(user.isPresent()){
            return user.get();
        } else {
            throw new CustomException("USER_NOT_FOUND");
        }
    }

    @Override
    @Transactional
    public void updateUser(String email, UserDTO userDTO) {
        // Fetch existing user by current email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException("User not found with email: " + email));

        // Update name
        if (userDTO.getName() != null) {
            user.setName(userDTO.getName());
        }

        // Update username if provided and changed
        if (userDTO.getUsername() != null && !userDTO.getUsername().equals(user.getUsername())) {
            // Check uniqueness: if another user has this username, and it's not the same user
            Optional<User> byUsername = userRepository.findByUsername(userDTO.getUsername());
            if (byUsername.isPresent() && !byUsername.get().getUserId().equals(user.getUserId())) {
                throw new IllegalArgumentException("Username already in use: " + userDTO.getUsername());
            }
            user.setUsername(userDTO.getUsername());
        }

        // Update email if provided and changed
        if (userDTO.getEmail() != null && !userDTO.getEmail().equals(user.getEmail())) {
            // Check uniqueness: if another user has this email
            Optional<User> byEmail = userRepository.findByEmail(userDTO.getEmail());
            if (byEmail.isPresent() && !byEmail.get().getUserId().equals(user.getUserId())) {
                throw new IllegalArgumentException("Email already in use: " + userDTO.getEmail());
            }
            user.setEmail(userDTO.getEmail());
        }

        // Update password if provided (non-blank)
        if (userDTO.getPassword() != null && !userDTO.getPassword().isBlank()) {
            String encoded = passwordEncoder.encode(userDTO.getPassword());
            user.setPassword(encoded);
        }

        // Update profileId if provided
        if (userDTO.getProfileId() != null) {
            user.setProfileId(userDTO.getProfileId());
        }



        // ... handle any other fields similarly ...

        // Save the updated entity. Because we're in a @Transactional method and `user` is managed,
        // JPA will flush changes on commit. But calling save() is okay too:
        userRepository.save(user);
        // Since the method returns void, we do not return anything here.
    }


    @Override
    @Transactional
    public User deleteUser(Long userId) {
        // Fetch the user or throw if not found
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("User not found with id: " + userId));
        
        // If there are related entities or constraints, handle them here if needed,
        // e.g., detach relations or check for orphan prevention.
        
        userRepository.delete(user);
        
        // After deletion, the `user` object is detached but still holds the data.
        // Return it if you need to show the deleted data; otherwise, consider returning void or a DTO.
        return user;
    }

    @Override
    public User getUserByUsername(String username) throws CustomException {
        Optional<User> user = userRepository.findByUsername(username);
        if(user.isPresent()){
            return user.get();
        } else {
            throw new CustomException("USER_NOT_FOUND");
        }
    }

    @Override
    public User getUserByEmail(String email) throws CustomException {
        Optional<User> user = userRepository.findByEmail(email);
        if(user.isPresent()){
            return user.get();
        } else {
            throw new CustomException("USER_NOT_FOUND");
        }
    }
    
}
