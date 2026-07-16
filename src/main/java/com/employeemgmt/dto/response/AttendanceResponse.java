package com.employeemgmt.dto.response;

import com.employeemgmt.entity.Attendance;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
public class AttendanceResponse {

    private Integer attId;
    private Integer empId;
    private String employeeName;
    private LocalDate attDate;
    private LocalTime checkIn;
    private LocalTime checkOut;
    private BigDecimal hoursWorked;
    private Attendance.Status status;
}
