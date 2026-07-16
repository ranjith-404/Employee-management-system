package com.employeemgmt.service;

import com.employeemgmt.entity.Attendance;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceService {

    Attendance checkIn(Integer empId);

    Attendance checkOut(Integer empId);

    Attendance getTodayAttendance(Integer empId);

    List<Attendance> getAttendanceByDateRange(Integer empId, LocalDate start, LocalDate end);

    List<Attendance> getAllAttendanceByDate(LocalDate date);

    List<Attendance> getEmployeeAttendanceHistory(Integer empId);
}
