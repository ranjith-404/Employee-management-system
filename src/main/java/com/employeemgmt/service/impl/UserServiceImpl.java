package com.employeemgmt.service.impl;

import com.employeemgmt.entity.User;
import com.employeemgmt.exception.ResourceNotFoundException;
import com.employeemgmt.exception.AuthenticationException;
import com.employeemgmt.exception.DuplicateResourceException;
import com.employeemgmt.repository.UserRepository;
import com.employeemgmt.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User createUser(User user) {
        if (userRepository.existsByUsernameIgnoreCase(user.getUsername())) {
            throw new DuplicateResourceException("User", "username", user.getUsername());
        }
        return userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public User authenticate(String username, String password) {
        User user = userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new AuthenticationException("Invalid username or password"));

        if (!user.getPassword().equals(password)) {
            throw new AuthenticationException("Invalid username or password");
        }

        if (!user.getIsActive()) {
            throw new AuthenticationException("Account is deactivated");
        }

        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserById(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserByUsername(String username) {
        return userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
    }

    @Override
    public User updateUser(Integer userId, User user) {
        User existing = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        existing.setUsername(user.getUsername());
        existing.setRole(user.getRole());
        existing.setIsActive(user.getIsActive());
        return userRepository.save(existing);
    }

    @Override
    public void deleteUser(Integer userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User", "id", userId);
        }
        userRepository.deleteById(userId);
    }
}
