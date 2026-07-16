package com.employeemgmt.controller;

import com.employeemgmt.dto.request.PayrollRequest;
import com.employeemgmt.dto.response.ApiResponse;
import com.employeemgmt.dto.response.PayrollResponse;
import com.employeemgmt.entity.Employee;
import com.employeemgmt.entity.Payroll;
import com.employeemgmt.mapper.Mapper;
import com.employeemgmt.service.EmployeeService;
import com.employeemgmt.service.PayrollService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/payroll")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PayrollController {

    private final PayrollService payrollService;
    private final EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<ApiResponse> generatePayroll(@Valid @RequestBody PayrollRequest request) {
        Employee employee = employeeService.getEmployeeById(request.getEmpId());

        Payroll payroll = Payroll.builder()
                .employee(employee)
                .payMonth(request.getPayMonth())
                .payYear(request.getPayYear())
                .basicSalary(request.getBasicSalary())
                .bonus(request.getBonus() != null ? request.getBonus() : java.math.BigDecimal.ZERO)
                .deductions(request.getDeductions() != null ? request.getDeductions() : java.math.BigDecimal.ZERO)
                .build();

        Payroll created = payrollService.generatePayroll(payroll);
        return new ResponseEntity<>(ApiResponse.success("Payroll generated",
                Mapper.toPayrollResponse(created)), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllPayroll(
            @RequestParam Integer month,
            @RequestParam Integer year) {
        List<PayrollResponse> payrolls = payrollService.getPayrollByMonthYear(month, year).stream()
                .map(Mapper::toPayrollResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("Payroll records retrieved", payrolls));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getPayrollById(@PathVariable Integer id) {
        Payroll payroll = payrollService.getPayrollById(id);
        return ResponseEntity.ok(ApiResponse.success("Payroll retrieved", Mapper.toPayrollResponse(payroll)));
    }

    @GetMapping("/employee/{empId}")
    public ResponseEntity<ApiResponse> getEmployeePayrollHistory(@PathVariable Integer empId) {
        List<PayrollResponse> payrolls = payrollService.getEmployeePayrollHistory(empId).stream()
                .map(Mapper::toPayrollResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("Employee payroll history retrieved", payrolls));
    }

    @GetMapping("/employee/{empId}/month/{month}/year/{year}")
    public ResponseEntity<ApiResponse> getEmployeePayrollByMonth(
            @PathVariable Integer empId,
            @PathVariable Integer month,
            @PathVariable Integer year) {
        Payroll payroll = payrollService.getEmployeePayrollByMonth(empId, month, year);
        return ResponseEntity.ok(ApiResponse.success("Payroll retrieved", Mapper.toPayrollResponse(payroll)));
    }

    @PutMapping("/{id}/mark-paid")
    public ResponseEntity<ApiResponse> markAsPaid(@PathVariable Integer id) {
        Payroll payroll = payrollService.markAsPaid(id);
        return ResponseEntity.ok(ApiResponse.success("Payroll marked as paid", Mapper.toPayrollResponse(payroll)));
    }
}
