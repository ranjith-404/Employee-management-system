package com.employeemgmt.dto.request;

import com.employeemgmt.entity.Leave;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class LeaveRequest {

    @NotNull(message = "Employee ID is required")
    private Integer empId;

    @NotNull(message = "Leave type is required")
    private Leave.LeaveType leaveType;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    private LocalDate endDate;

    private String reason;
}
