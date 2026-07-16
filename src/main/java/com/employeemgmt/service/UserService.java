package com.employeemgmt.service;

import com.employeemgmt.entity.User;

import java.util.List;

public interface UserService {

    User createUser(User user);

    List<User> getAllUsers();

    User authenticate(String username, String password);

    User getUserById(Integer userId);

    User getUserByUsername(String username);

    User updateUser(Integer userId, User user);

    void deleteUser(Integer userId);
}
