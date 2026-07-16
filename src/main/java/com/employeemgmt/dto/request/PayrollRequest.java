package com.employeemgmt.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PayrollRequest {

    @NotNull(message = "Employee ID is required")
    private Integer empId;

    @NotNull(message = "Pay month is required")
    private Integer payMonth;

    @NotNull(message = "Pay year is required")
    private Integer payYear;

    @NotNull(message = "Basic salary is required")
    private BigDecimal basicSalary;

    private BigDecimal bonus;

    private BigDecimal deductions;
}
