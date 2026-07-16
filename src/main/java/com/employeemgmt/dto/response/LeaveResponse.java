package com.employeemgmt.dto.response;

import com.employeemgmt.entity.Leave;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class LeaveResponse {

    private Integer leaveId;
    private Integer empId;
    private String employeeName;
    private Leave.LeaveType leaveType;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer days;
    private String reason;
    private Leave.Status status;
    private String approvedByName;
    private LocalDateTime createdAt;
}
