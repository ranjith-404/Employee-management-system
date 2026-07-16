package com.employeemgmt.repository;

import com.employeemgmt.entity.Payroll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PayrollRepository extends JpaRepository<Payroll, Integer> {

    Optional<Payroll> findByEmployee_EmpIdAndPayMonthAndPayYear(
            Integer empId, Integer payMonth, Integer payYear);

    List<Payroll> findByPayMonthAndPayYear(Integer payMonth, Integer payYear);

    List<Payroll> findByEmployee_EmpIdOrderByPayYearDescPayMonthDesc(Integer empId);
}
