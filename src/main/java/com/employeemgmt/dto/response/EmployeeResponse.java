package com.employeemgmt.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class EmployeeResponse {

    private Integer empId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private LocalDate hireDate;
    private String photoUrl;
    private Boolean isActive;
    private String departmentName;
    private Integer deptId;
    private LocalDateTime createdAt;
}
