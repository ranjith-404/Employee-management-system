package com.employeemgmt.service;

import com.employeemgmt.entity.Leave;

import java.util.List;

public interface LeaveService {

    Leave applyLeave(Leave leave);

    Leave approveLeave(Integer leaveId, Integer approvedByUserId);

    Leave rejectLeave(Integer leaveId, Integer approvedByUserId);

    Leave getLeaveById(Integer leaveId);

    List<Leave> getLeavesByEmployee(Integer empId);

    List<Leave> getPendingLeaves();

    List<Leave> getLeavesByStatus(Leave.Status status);
}
