package com.employeemgmt.service.impl;

import com.employeemgmt.entity.Department;
import com.employeemgmt.exception.ResourceNotFoundException;
import com.employeemgmt.exception.DuplicateResourceException;
import com.employeemgmt.repository.DepartmentRepository;
import com.employeemgmt.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    @Override
    public Department createDepartment(Department department) {
        if (departmentRepository.existsByDeptNameIgnoreCase(department.getDeptName())) {
            throw new DuplicateResourceException("Department", "name", department.getDeptName());
        }
        return departmentRepository.save(department);
    }

    @Override
    public Department updateDepartment(Integer deptId, Department department) {
        Department existing = departmentRepository.findById(deptId)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", deptId));

        existing.setDeptName(department.getDeptName());
        existing.setLocation(department.getLocation());
        return departmentRepository.save(existing);
    }

    @Override
    public void deleteDepartment(Integer deptId) {
        if (!departmentRepository.existsById(deptId)) {
            throw new ResourceNotFoundException("Department", "id", deptId);
        }
        departmentRepository.deleteById(deptId);
    }

    @Override
    @Transactional(readOnly = true)
    public Department getDepartmentById(Integer deptId) {
        return departmentRepository.findById(deptId)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", deptId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByName(String deptName) {
        return departmentRepository.existsByDeptNameIgnoreCase(deptName);
    }
}
