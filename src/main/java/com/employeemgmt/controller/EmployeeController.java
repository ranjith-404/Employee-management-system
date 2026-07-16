package com.employeemgmt.controller;

import com.employeemgmt.dto.request.EmployeeRequest;
import com.employeemgmt.dto.response.ApiResponse;
import com.employeemgmt.dto.response.EmployeeResponse;
import com.employeemgmt.entity.Employee;
import com.employeemgmt.mapper.Mapper;
import com.employeemgmt.service.DepartmentService;
import com.employeemgmt.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final DepartmentService departmentService;

    @PostMapping
    public ResponseEntity<ApiResponse> createEmployee(@Valid @RequestBody EmployeeRequest request) {
        Employee employee = Employee.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .hireDate(request.getHireDate())
                .department(request.getDeptId() != null
                        ? departmentService.getDepartmentById(request.getDeptId())
                        : null)
                .build();

        Employee created = employeeService.createEmployee(employee);
        return new ResponseEntity<>(ApiResponse.success("Employee created",
                Mapper.toEmployeeResponse(created)), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllEmployees() {
        List<EmployeeResponse> employees = employeeService.getAllEmployees().stream()
                .map(Mapper::toEmployeeResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("Employees retrieved", employees));
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse> getActiveEmployees() {
        List<EmployeeResponse> employees = employeeService.getActiveEmployees().stream()
                .map(Mapper::toEmployeeResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("Active employees retrieved", employees));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getEmployeeById(@PathVariable Integer id) {
        Employee employee = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(ApiResponse.success("Employee retrieved",
                Mapper.toEmployeeResponse(employee)));
    }

    @GetMapping("/department/{deptId}")
    public ResponseEntity<ApiResponse> getEmployeesByDepartment(@PathVariable Integer deptId) {
        List<EmployeeResponse> employees = employeeService.getEmployeesByDepartment(deptId).stream()
                .map(Mapper::toEmployeeResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("Employees retrieved", employees));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse> searchEmployees(@RequestParam String keyword) {
        List<EmployeeResponse> employees = employeeService.searchEmployees(keyword).stream()
                .map(Mapper::toEmployeeResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("Search results", employees));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateEmployee(@PathVariable Integer id,
                                                      @Valid @RequestBody EmployeeRequest request) {
        Employee employee = Employee.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .hireDate(request.getHireDate())
                .department(request.getDeptId() != null
                        ? departmentService.getDepartmentById(request.getDeptId())
                        : null)
                .build();

        Employee updated = employeeService.updateEmployee(id, employee);
        return ResponseEntity.ok(ApiResponse.success("Employee updated",
                Mapper.toEmployeeResponse(updated)));
    }

    @PatchMapping("/{id}/toggle-status")
    public ResponseEntity<ApiResponse> toggleStatus(@PathVariable Integer id) {
        Employee employee = employeeService.toggleActiveStatus(id);
        return ResponseEntity.ok(ApiResponse.success("Employee status toggled",
                Mapper.toEmployeeResponse(employee)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteEmployee(@PathVariable Integer id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.ok(ApiResponse.success("Employee deleted"));
    }
}
