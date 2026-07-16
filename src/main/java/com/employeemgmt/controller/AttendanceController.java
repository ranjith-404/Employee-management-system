package com.employeemgmt.controller;

import com.employeemgmt.dto.response.ApiResponse;
import com.employeemgmt.dto.response.AttendanceResponse;
import com.employeemgmt.entity.Attendance;
import com.employeemgmt.mapper.Mapper;
import com.employeemgmt.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AttendanceController {

    private final AttendanceService attendanceService;

    @PostMapping("/check-in/{empId}")
    public ResponseEntity<ApiResponse> checkIn(@PathVariable Integer empId) {
        Attendance attendance = attendanceService.checkIn(empId);
        return ResponseEntity.ok(ApiResponse.success("Checked in successfully",
                Mapper.toAttendanceResponse(attendance)));
    }

    @PostMapping("/check-out/{empId}")
    public ResponseEntity<ApiResponse> checkOut(@PathVariable Integer empId) {
        Attendance attendance = attendanceService.checkOut(empId);
        return ResponseEntity.ok(ApiResponse.success("Checked out successfully",
                Mapper.toAttendanceResponse(attendance)));
    }

    @GetMapping("/today/{empId}")
    public ResponseEntity<ApiResponse> getTodayAttendance(@PathVariable Integer empId) {
        Attendance attendance = attendanceService.getTodayAttendance(empId);
        if (attendance == null) {
            return ResponseEntity.ok(ApiResponse.success("No attendance record for today", null));
        }
        return ResponseEntity.ok(ApiResponse.success("Attendance retrieved",
                Mapper.toAttendanceResponse(attendance)));
    }

    @GetMapping("/employee/{empId}")
    public ResponseEntity<ApiResponse> getEmployeeAttendance(
            @PathVariable Integer empId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        List<AttendanceResponse> records = attendanceService.getAttendanceByDateRange(empId, start, end).stream()
                .map(Mapper::toAttendanceResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("Attendance records retrieved", records));
    }

    @GetMapping("/employee/{empId}/history")
    public ResponseEntity<ApiResponse> getAttendanceHistory(@PathVariable Integer empId) {
        List<AttendanceResponse> records = attendanceService.getEmployeeAttendanceHistory(empId).stream()
                .map(Mapper::toAttendanceResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("Attendance history retrieved", records));
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<ApiResponse> getAllAttendanceByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<AttendanceResponse> records = attendanceService.getAllAttendanceByDate(date).stream()
                .map(Mapper::toAttendanceResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("Attendance records retrieved", records));
    }
}
