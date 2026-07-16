package com.employeemgmt.mapper;

import com.employeemgmt.dto.response.*;
import com.employeemgmt.entity.*;

public class Mapper {

    public static EmployeeResponse toEmployeeResponse(Employee employee) {
        return EmployeeResponse.builder()
                .empId(employee.getEmpId())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .email(employee.getEmail())
                .phone(employee.getPhone())
                .hireDate(employee.getHireDate())
                .photoUrl(employee.getPhotoUrl())
                .isActive(employee.getIsActive())
                .deptId(employee.getDepartment() != null ? employee.getDepartment().getDeptId() : null)
                .departmentName(employee.getDepartment() != null ? employee.getDepartment().getDeptName() : null)
                .createdAt(employee.getCreatedAt())
                .build();
    }

    public static DepartmentResponse toDepartmentResponse(Department department, int employeeCount) {
        return DepartmentResponse.builder()
                .deptId(department.getDeptId())
                .deptName(department.getDeptName())
                .location(department.getLocation())
                .employeeCount(employeeCount)
                .createdAt(department.getCreatedAt())
                .build();
    }

    public static UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .role(user.getRole())
                .isActive(user.getIsActive())
                .empId(user.getEmployee() != null ? user.getEmployee().getEmpId() : null)
                .employeeName(user.getEmployee() != null
                        ? user.getEmployee().getFirstName() + " " + user.getEmployee().getLastName()
                        : null)
                .createdAt(user.getCreatedAt())
                .build();
    }

    public static AttendanceResponse toAttendanceResponse(Attendance attendance) {
        return AttendanceResponse.builder()
                .attId(attendance.getAttId())
                .empId(attendance.getEmployee().getEmpId())
                .employeeName(attendance.getEmployee().getFirstName() + " " + attendance.getEmployee().getLastName())
                .attDate(attendance.getAttDate())
                .checkIn(attendance.getCheckIn())
                .checkOut(attendance.getCheckOut())
                .hoursWorked(attendance.getHoursWorked())
                .status(attendance.getStatus())
                .build();
    }

    public static LeaveResponse toLeaveResponse(Leave leave) {
        return LeaveResponse.builder()
                .leaveId(leave.getLeaveId())
                .empId(leave.getEmployee().getEmpId())
                .employeeName(leave.getEmployee().getFirstName() + " " + leave.getEmployee().getLastName())
                .leaveType(leave.getLeaveType())
                .startDate(leave.getStartDate())
                .endDate(leave.getEndDate())
                .days(leave.getDays())
                .reason(leave.getReason())
                .status(leave.getStatus())
                .approvedByName(leave.getApprovedBy() != null ? leave.getApprovedBy().getUsername() : null)
                .createdAt(leave.getCreatedAt())
                .build();
    }

    public static PayrollResponse toPayrollResponse(Payroll payroll) {
        return PayrollResponse.builder()
                .payId(payroll.getPayId())
                .empId(payroll.getEmployee().getEmpId())
                .employeeName(payroll.getEmployee().getFirstName() + " " + payroll.getEmployee().getLastName())
                .payMonth(payroll.getPayMonth())
                .payYear(payroll.getPayYear())
                .basicSalary(payroll.getBasicSalary())
                .bonus(payroll.getBonus())
                .deductions(payroll.getDeductions())
                .netSalary(payroll.getNetSalary())
                .paidDate(payroll.getPaidDate())
                .createdAt(payroll.getCreatedAt())
                .build();
    }
}
