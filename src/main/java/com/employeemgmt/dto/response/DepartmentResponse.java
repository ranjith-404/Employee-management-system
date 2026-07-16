package com.employeemgmt.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class DepartmentResponse {

    private Integer deptId;
    private String deptName;
    private String location;
    private Integer employeeCount;
    private LocalDateTime createdAt;
}
