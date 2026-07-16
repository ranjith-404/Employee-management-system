package com.employeemgmt.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class PayrollResponse {

    private Integer payId;
    private Integer empId;
    private String employeeName;
    private Integer payMonth;
    private Integer payYear;
    private BigDecimal basicSalary;
    private BigDecimal bonus;
    private BigDecimal deductions;
    private BigDecimal netSalary;
    private LocalDate paidDate;
    private LocalDateTime createdAt;
}
