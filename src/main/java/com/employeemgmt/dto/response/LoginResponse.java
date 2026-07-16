package com.employeemgmt.dto.response;

import com.employeemgmt.entity.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {

    private Integer userId;
    private String username;
    private User.Role role;
    private Integer empId;
    private String employeeName;
    private String message;
}
