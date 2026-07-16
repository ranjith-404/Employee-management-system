package com.employeemgmt.service.impl;

import com.employeemgmt.entity.Attendance;
import com.employeemgmt.exception.ResourceNotFoundException;
import com.employeemgmt.exception.BusinessRuleException;
import com.employeemgmt.repository.AttendanceRepository;
import com.employeemgmt.repository.EmployeeRepository;
import com.employeemgmt.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public Attendance checkIn(Integer empId) {
        if (!employeeRepository.existsById(empId)) {
            throw new ResourceNotFoundException("Employee", "id", empId);
        }

        LocalDate today = LocalDate.now();
        attendanceRepository.findByEmployee_EmpIdAndAttDate(empId, today)
                .ifPresent(a -> {
                    throw new BusinessRuleException("Already checked in today");
                });

        Attendance attendance = Attendance.builder()
                .employee(employeeRepository.getReferenceById(empId))
                .attDate(today)
                .checkIn(LocalTime.now())
                .status(Attendance.Status.PRESENT)
                .build();

        return attendanceRepository.save(attendance);
    }

    @Override
    public Attendance checkOut(Integer empId) {
        LocalDate today = LocalDate.now();
        Attendance attendance = attendanceRepository
                .findByEmployee_EmpIdAndAttDate(empId, today)
                .orElseThrow(() -> new BusinessRuleException("No check-in found for today"));

        if (attendance.getCheckOut() != null) {
            throw new BusinessRuleException("Already checked out today");
        }

        attendance.setCheckOut(LocalTime.now());

        long minutes = Duration.between(attendance.getCheckIn(), attendance.getCheckOut()).toMinutes();
        BigDecimal hours = BigDecimal.valueOf(minutes / 60.0);
        attendance.setHoursWorked(hours);

        return attendanceRepository.save(attendance);
    }

    @Override
    @Transactional(readOnly = true)
    public Attendance getTodayAttendance(Integer empId) {
        return attendanceRepository.findByEmployee_EmpIdAndAttDate(empId, LocalDate.now())
                .orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Attendance> getAttendanceByDateRange(Integer empId, LocalDate start, LocalDate end) {
        return attendanceRepository.findByEmployee_EmpIdAndAttDateBetween(empId, start, end);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Attendance> getAllAttendanceByDate(LocalDate date) {
        return attendanceRepository.findByAttDate(date);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Attendance> getEmployeeAttendanceHistory(Integer empId) {
        return attendanceRepository.findByEmployee_EmpIdOrderByAttDateDesc(empId);
    }
}
