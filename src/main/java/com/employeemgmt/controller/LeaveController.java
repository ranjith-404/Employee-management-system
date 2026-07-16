package com.employeemgmt.controller;

import com.employeemgmt.dto.request.LeaveRequest;
import com.employeemgmt.dto.response.ApiResponse;
import com.employeemgmt.dto.response.LeaveResponse;
import com.employeemgmt.entity.Employee;
import com.employeemgmt.entity.Leave;
import com.employeemgmt.mapper.Mapper;
import com.employeemgmt.service.EmployeeService;
import com.employeemgmt.service.LeaveService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/leaves")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class LeaveController {

    private final LeaveService leaveService;
    private final EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<ApiResponse> applyLeave(@Valid @RequestBody LeaveRequest request) {
        Employee employee = employeeService.getEmployeeById(request.getEmpId());

        Leave leave = Leave.builder()
                .employee(employee)
                .leaveType(request.getLeaveType())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .reason(request.getReason())
                .build();

        Leave created = leaveService.applyLeave(leave);
        return new ResponseEntity<>(ApiResponse.success("Leave applied successfully",
                Mapper.toLeaveResponse(created)), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllLeaves() {
        List<LeaveResponse> leaves = leaveService.getLeavesByStatus(Leave.Status.PENDING).stream()
                .map(Mapper::toLeaveResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("Leaves retrieved", leaves));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getLeaveById(@PathVariable Integer id) {
        Leave leave = leaveService.getLeaveById(id);
        return ResponseEntity.ok(ApiResponse.success("Leave retrieved", Mapper.toLeaveResponse(leave)));
    }

    @GetMapping("/employee/{empId}")
    public ResponseEntity<ApiResponse> getEmployeeLeaves(@PathVariable Integer empId) {
        List<LeaveResponse> leaves = leaveService.getLeavesByEmployee(empId).stream()
                .map(Mapper::toLeaveResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("Employee leaves retrieved", leaves));
    }

    @GetMapping("/pending")
    public ResponseEntity<ApiResponse> getPendingLeaves() {
        List<LeaveResponse> leaves = leaveService.getPendingLeaves().stream()
                .map(Mapper::toLeaveResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("Pending leaves retrieved", leaves));
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<ApiResponse> approveLeave(@PathVariable Integer id,
                                                     @RequestParam Integer approvedBy) {
        Leave leave = leaveService.approveLeave(id, approvedBy);
        return ResponseEntity.ok(ApiResponse.success("Leave approved", Mapper.toLeaveResponse(leave)));
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<ApiResponse> rejectLeave(@PathVariable Integer id,
                                                    @RequestParam Integer approvedBy) {
        Leave leave = leaveService.rejectLeave(id, approvedBy);
        return ResponseEntity.ok(ApiResponse.success("Leave rejected", Mapper.toLeaveResponse(leave)));
    }
}
