package com.employeemgmt.service.impl;

import com.employeemgmt.entity.Employee;
import com.employeemgmt.exception.ResourceNotFoundException;
import com.employeemgmt.exception.DuplicateResourceException;
import com.employeemgmt.repository.EmployeeRepository;
import com.employeemgmt.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Override
    public Employee createEmployee(Employee employee) {
        if (employeeRepository.existsByEmailIgnoreCase(employee.getEmail())) {
            throw new DuplicateResourceException("Employee", "email", employee.getEmail());
        }
        return employeeRepository.save(employee);
    }

    @Override
    public Employee updateEmployee(Integer empId, Employee employee) {
        Employee existing = employeeRepository.findById(empId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", empId));

        existing.setFirstName(employee.getFirstName());
        existing.setLastName(employee.getLastName());
        existing.setEmail(employee.getEmail());
        existing.setPhone(employee.getPhone());
        existing.setHireDate(employee.getHireDate());
        existing.setDepartment(employee.getDepartment());
        existing.setPhotoUrl(employee.getPhotoUrl());
        return employeeRepository.save(existing);
    }

    @Override
    public void deleteEmployee(Integer empId) {
        if (!employeeRepository.existsById(empId)) {
            throw new ResourceNotFoundException("Employee", "id", empId);
        }
        employeeRepository.deleteById(empId);
    }

    @Override
    @Transactional(readOnly = true)
    public Employee getEmployeeById(Integer empId) {
        return employeeRepository.findById(empId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", empId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Employee> getActiveEmployees() {
        return employeeRepository.findByIsActiveTrue();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Employee> getEmployeesByDepartment(Integer deptId) {
        return employeeRepository.findByDepartment_DeptId(deptId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Employee> searchEmployees(String keyword) {
        return employeeRepository
                .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(keyword, keyword);
    }

    @Override
    public Employee toggleActiveStatus(Integer empId) {
        Employee employee = employeeRepository.findById(empId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", empId));
        employee.setIsActive(!employee.getIsActive());
        return employeeRepository.save(employee);
    }
}
