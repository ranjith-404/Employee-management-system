package com.employeemgmt.controller;

import com.employeemgmt.dto.request.UserRequest;
import com.employeemgmt.dto.response.ApiResponse;
import com.employeemgmt.dto.response.UserResponse;
import com.employeemgmt.entity.User;
import com.employeemgmt.mapper.Mapper;
import com.employeemgmt.service.EmployeeService;
import com.employeemgmt.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;
    private final EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<ApiResponse> createUser(@Valid @RequestBody UserRequest request) {
        User user = User.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .role(request.getRole())
                .employee(request.getEmpId() != null
                        ? employeeService.getEmployeeById(request.getEmpId())
                        : null)
                .build();

        User created = userService.createUser(user);
        return new ResponseEntity<>(ApiResponse.success("User created", Mapper.toUserResponse(created)),
                HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllUsers() {
        List<UserResponse> users = userService.getAllUsers().stream()
                .map(Mapper::toUserResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("Users retrieved", users));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable Integer id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(ApiResponse.success("User retrieved", Mapper.toUserResponse(user)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateUser(@PathVariable Integer id,
                                                  @Valid @RequestBody UserRequest request) {
        User user = User.builder()
                .username(request.getUsername())
                .role(request.getRole())
                .employee(request.getEmpId() != null
                        ? employeeService.getEmployeeById(request.getEmpId())
                        : null)
                .build();

        User updated = userService.updateUser(id, user);
        return ResponseEntity.ok(ApiResponse.success("User updated", Mapper.toUserResponse(updated)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.success("User deleted"));
    }
}
