package com.employeemgmt.service;

import com.employeemgmt.entity.Employee;

import java.util.List;

public interface EmployeeService {

    Employee createEmployee(Employee employee);

    Employee updateEmployee(Integer empId, Employee employee);

    void deleteEmployee(Integer empId);

    Employee getEmployeeById(Integer empId);

    List<Employee> getAllEmployees();

    List<Employee> getActiveEmployees();

    List<Employee> getEmployeesByDepartment(Integer deptId);

    List<Employee> searchEmployees(String keyword);

    Employee toggleActiveStatus(Integer empId);
}
