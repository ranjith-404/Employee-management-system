package com.employeemgmt.controller;

import com.employeemgmt.dto.request.LoginRequest;
import com.employeemgmt.dto.response.ApiResponse;
import com.employeemgmt.dto.response.LoginResponse;
import com.employeemgmt.entity.User;
import com.employeemgmt.mapper.Mapper;
import com.employeemgmt.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest request) {
        User user = userService.authenticate(request.getUsername(), request.getPassword());

        LoginResponse response = LoginResponse.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .role(user.getRole())
                .empId(user.getEmployee() != null ? user.getEmployee().getEmpId() : null)
                .employeeName(user.getEmployee() != null
                        ? user.getEmployee().getFirstName() + " " + user.getEmployee().getLastName()
                        : null)
                .message("Login successful")
                .build();

        return ResponseEntity.ok(ApiResponse.success("Login successful", response));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody User user) {
        User created = userService.createUser(user);
        return ResponseEntity.ok(ApiResponse.success("User registered successfully", Mapper.toUserResponse(created)));
    }
}
