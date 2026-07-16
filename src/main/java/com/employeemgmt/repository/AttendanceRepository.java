package com.employeemgmt.repository;

import com.employeemgmt.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Integer> {

    Optional<Attendance> findByEmployee_EmpIdAndAttDate(Integer empId, LocalDate attDate);

    List<Attendance> findByEmployee_EmpIdAndAttDateBetween(
            Integer empId, LocalDate startDate, LocalDate endDate);

    List<Attendance> findByAttDate(LocalDate attDate);

    List<Attendance> findByEmployee_EmpIdOrderByAttDateDesc(Integer empId);
}
