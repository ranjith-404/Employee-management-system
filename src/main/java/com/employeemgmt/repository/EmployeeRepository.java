package com.employeemgmt.repository;

import com.employeemgmt.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    Optional<Employee> findByEmailIgnoreCase(String email);

    List<Employee> findByIsActiveTrue();

    List<Employee> findByDepartment_DeptId(Integer deptId);

    List<Employee> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
            String firstName, String lastName);

    boolean existsByEmailIgnoreCase(String email);
}
