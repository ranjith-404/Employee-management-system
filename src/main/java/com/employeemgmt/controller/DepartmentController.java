package com.employeemgmt.controller;

import com.employeemgmt.dto.request.DepartmentRequest;
import com.employeemgmt.dto.response.ApiResponse;
import com.employeemgmt.dto.response.DepartmentResponse;
import com.employeemgmt.entity.Department;
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
@RequestMapping("/api/departments")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DepartmentController {

    private final DepartmentService departmentService;
    private final EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<ApiResponse> createDepartment(@Valid @RequestBody DepartmentRequest request) {
        Department department = Department.builder()
                .deptName(request.getDeptName())
                .location(request.getLocation())
                .build();

        Department created = departmentService.createDepartment(department);
        return new ResponseEntity<>(ApiResponse.success("Department created",
                Mapper.toDepartmentResponse(created, 0)), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllDepartments() {
        List<DepartmentResponse> departments = departmentService.getAllDepartments().stream()
                .map(d -> Mapper.toDepartmentResponse(d, employeeService.getEmployeesByDepartment(d.getDeptId()).size()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("Departments retrieved", departments));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getDepartmentById(@PathVariable Integer id) {
        Department department = departmentService.getDepartmentById(id);
        int empCount = employeeService.getEmployeesByDepartment(id).size();
        return ResponseEntity.ok(ApiResponse.success("Department retrieved",
                Mapper.toDepartmentResponse(department, empCount)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateDepartment(@PathVariable Integer id,
                                                        @Valid @RequestBody DepartmentRequest request) {
        Department department = Department.builder()
                .deptName(request.getDeptName())
                .location(request.getLocation())
                .build();

        Department updated = departmentService.updateDepartment(id, department);
        return ResponseEntity.ok(ApiResponse.success("Department updated",
                Mapper.toDepartmentResponse(updated, 0)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteDepartment(@PathVariable Integer id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.ok(ApiResponse.success("Department deleted"));
    }
}
