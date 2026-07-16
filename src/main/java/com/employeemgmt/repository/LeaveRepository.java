package com.employeemgmt.repository;

import com.employeemgmt.entity.Leave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaveRepository extends JpaRepository<Leave, Integer> {

    List<Leave> findByEmployee_EmpIdOrderByCreatedAtDesc(Integer empId);

    List<Leave> findByStatus(Leave.Status status);

    List<Leave> findByEmployee_EmpIdAndStatus(Integer empId, Leave.Status status);
}
