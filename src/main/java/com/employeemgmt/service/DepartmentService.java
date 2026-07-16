package com.employeemgmt.service;

import com.employeemgmt.entity.Department;

import java.util.List;

public interface DepartmentService {

    Department createDepartment(Department department);

    Department updateDepartment(Integer deptId, Department department);

    void deleteDepartment(Integer deptId);

    Department getDepartmentById(Integer deptId);

    List<Department> getAllDepartments();

    boolean existsByName(String deptName);
}
