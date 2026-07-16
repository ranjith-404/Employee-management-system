package com.employeemgmt.service;

import com.employeemgmt.entity.Payroll;

import java.util.List;

public interface PayrollService {

    Payroll generatePayroll(Payroll payroll);

    Payroll getPayrollById(Integer payId);

    List<Payroll> getPayrollByMonthYear(Integer month, Integer year);

    List<Payroll> getEmployeePayrollHistory(Integer empId);

    Payroll getEmployeePayrollByMonth(Integer empId, Integer month, Integer year);

    Payroll markAsPaid(Integer payId);
}
