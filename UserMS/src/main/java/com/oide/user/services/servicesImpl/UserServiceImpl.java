package com.oide.user.services.servicesImpl;

import java.util.Optional;
import java.util.UUID;

import com.oide.user.services.ApiService;
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

    private final UserRepository    userRepository;
    private final ApiService        apiService;
    private final PasswordEncoder   passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           ApiService apiService,
                           PasswordEncoder passwordEncoder) {
        this.userRepository   = userRepository;
        this.apiService       = apiService;
        this.passwordEncoder  = passwordEncoder;
    }

    @Override
    public void createUser(UserDTO userDTO) throws CustomException {
        Optional<User> existing = userRepository.findByEmail(userDTO.getEmail());
        if (existing.isPresent()) {
            throw new CustomException("USER_ALREADY_EXISTS");
        }

        Long userId = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
        User user = new User();
        user.setUserId(userId);
        user.setName(userDTO.getName());
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        // 🔐 encode password before saving
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        Long profileId = apiService.addProfile(userDTO).block();
        user.setProfileId(profileId);

        userRepository.save(user);
    }

    @Override
    public UserDTO loginUser(UserDTO userDTO) throws CustomException {
        // 1) find user by email
        User user = userRepository.findByEmail(userDTO.getEmail())
                .orElseThrow(() -> new CustomException("INVALID_CREDENTIALS"));

        // 2) check password
        if (!passwordEncoder.matches(userDTO.getPassword(), user.getPassword())) {
            throw new CustomException("INVALID_CREDENTIALS");
        }

        // 3) return basic info as DTO (you may omit the password in DTO)
        UserDTO dto = user.toDTO();
        dto.setPassword(null);  // never expose the hash
        return dto;
    }

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("USER_NOT_FOUND"));
    }

    @Override
    @Transactional
    public void updateUser(String email, UserDTO userDTO) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException("USER_NOT_FOUND"));

        // name
        if (userDTO.getName() != null) {
            user.setName(userDTO.getName());
        }

        // username
        if (userDTO.getUsername() != null && !userDTO.getUsername().equals(user.getUsername())) {
            userRepository.findByUsername(userDTO.getUsername())
                    .filter(u -> !u.getUserId().equals(user.getUserId()))
                    .ifPresent(u -> { throw new CustomException("USERNAME_ALREADY_IN_USE"); });
            user.setUsername(userDTO.getUsername());
        }

        // email
        if (userDTO.getEmail() != null && !userDTO.getEmail().equals(user.getEmail())) {
            userRepository.findByEmail(userDTO.getEmail())
                    .filter(u -> !u.getUserId().equals(user.getUserId()))
                    .ifPresent(u -> { throw new CustomException("EMAIL_ALREADY_IN_USE"); });
            user.setEmail(userDTO.getEmail());
        }

        // password (if provided)
        if (userDTO.getPassword() != null && !userDTO.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }

        // profileId
        if (userDTO.getProfileId() != null) {
            user.setProfileId(userDTO.getProfileId());
        }

        userRepository.save(user);
    }

    @Override
    @Transactional
    public User deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("USER_NOT_FOUND"));
        userRepository.delete(user);
        return user;
    }

    @Override
    public User getUserByUsername(String username) throws CustomException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException("USER_NOT_FOUND"));
    }

    @Override
    public User getUserByEmail(String email) throws CustomException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException("USER_NOT_FOUND"));
    }
}
