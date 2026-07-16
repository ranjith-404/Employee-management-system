package com.employeemgmt.dto.response;

import com.employeemgmt.entity.User;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserResponse {

    private Integer userId;
    private String username;
    private User.Role role;
    private Boolean isActive;
    private Integer empId;
    private String employeeName;
    private LocalDateTime createdAt;
}
