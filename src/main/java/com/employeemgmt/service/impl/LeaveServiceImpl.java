package com.employeemgmt.service.impl;

import com.employeemgmt.entity.Leave;
import com.employeemgmt.exception.ResourceNotFoundException;
import com.employeemgmt.exception.BusinessRuleException;
import com.employeemgmt.repository.EmployeeRepository;
import com.employeemgmt.repository.LeaveRepository;
import com.employeemgmt.repository.UserRepository;
import com.employeemgmt.service.LeaveService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class LeaveServiceImpl implements LeaveService {

    private final LeaveRepository leaveRepository;
    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;

    @Override
    public Leave applyLeave(Leave leave) {
        if (!employeeRepository.existsById(leave.getEmployee().getEmpId())) {
            throw new ResourceNotFoundException("Employee", "id", leave.getEmployee().getEmpId());
        }

        if (leave.getEndDate().isBefore(leave.getStartDate())) {
            throw new BusinessRuleException("End date cannot be before start date");
        }

        long days = ChronoUnit.DAYS.between(leave.getStartDate(), leave.getEndDate()) + 1;
        leave.setDays((int) days);
        leave.setStatus(Leave.Status.PENDING);

        return leaveRepository.save(leave);
    }

    @Override
    public Leave approveLeave(Integer leaveId, Integer approvedByUserId) {
        Leave leave = leaveRepository.findById(leaveId)
                .orElseThrow(() -> new ResourceNotFoundException("Leave", "id", leaveId));

        if (leave.getStatus() != Leave.Status.PENDING) {
            throw new BusinessRuleException("Only pending leaves can be approved");
        }

        leave.setStatus(Leave.Status.APPROVED);
        leave.setApprovedBy(userRepository.getReferenceById(approvedByUserId));
        return leaveRepository.save(leave);
    }

    @Override
    public Leave rejectLeave(Integer leaveId, Integer approvedByUserId) {
        Leave leave = leaveRepository.findById(leaveId)
                .orElseThrow(() -> new ResourceNotFoundException("Leave", "id", leaveId));

        if (leave.getStatus() != Leave.Status.PENDING) {
            throw new BusinessRuleException("Only pending leaves can be rejected");
        }

        leave.setStatus(Leave.Status.REJECTED);
        leave.setApprovedBy(userRepository.getReferenceById(approvedByUserId));
        return leaveRepository.save(leave);
    }

    @Override
    @Transactional(readOnly = true)
    public Leave getLeaveById(Integer leaveId) {
        return leaveRepository.findById(leaveId)
                .orElseThrow(() -> new ResourceNotFoundException("Leave", "id", leaveId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Leave> getLeavesByEmployee(Integer empId) {
        return leaveRepository.findByEmployee_EmpIdOrderByCreatedAtDesc(empId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Leave> getPendingLeaves() {
        return leaveRepository.findByStatus(Leave.Status.PENDING);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Leave> getLeavesByStatus(Leave.Status status) {
        return leaveRepository.findByStatus(status);
    }
}
