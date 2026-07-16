package com.employeemgmt.service.impl;

import com.employeemgmt.entity.Payroll;
import com.employeemgmt.exception.ResourceNotFoundException;
import com.employeemgmt.exception.BusinessRuleException;
import com.employeemgmt.repository.EmployeeRepository;
import com.employeemgmt.repository.PayrollRepository;
import com.employeemgmt.service.PayrollService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PayrollServiceImpl implements PayrollService {

    private final PayrollRepository payrollRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public Payroll generatePayroll(Payroll payroll) {
        if (!employeeRepository.existsById(payroll.getEmployee().getEmpId())) {
            throw new ResourceNotFoundException("Employee", "id", payroll.getEmployee().getEmpId());
        }

        payrollRepository.findByEmployee_EmpIdAndPayMonthAndPayYear(
                payroll.getEmployee().getEmpId(), payroll.getPayMonth(), payroll.getPayYear())
                .ifPresent(p -> {
                    throw new BusinessRuleException("Payroll already exists for this employee for the given month/year");
                });

        return payrollRepository.save(payroll);
    }

    @Override
    @Transactional(readOnly = true)
    public Payroll getPayrollById(Integer payId) {
        return payrollRepository.findById(payId)
                .orElseThrow(() -> new ResourceNotFoundException("Payroll", "id", payId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Payroll> getPayrollByMonthYear(Integer month, Integer year) {
        return payrollRepository.findByPayMonthAndPayYear(month, year);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Payroll> getEmployeePayrollHistory(Integer empId) {
        return payrollRepository.findByEmployee_EmpIdOrderByPayYearDescPayMonthDesc(empId);
    }

    @Override
    @Transactional(readOnly = true)
    public Payroll getEmployeePayrollByMonth(Integer empId, Integer month, Integer year) {
        return payrollRepository.findByEmployee_EmpIdAndPayMonthAndPayYear(empId, month, year)
                .orElseThrow(() -> new ResourceNotFoundException("Payroll", "employee/month/year",
                        empId + "/" + month + "/" + year));
    }

    @Override
    public Payroll markAsPaid(Integer payId) {
        Payroll payroll = payrollRepository.findById(payId)
                .orElseThrow(() -> new ResourceNotFoundException("Payroll", "id", payId));

        if (payroll.getPaidDate() != null) {
            throw new BusinessRuleException("Payroll already marked as paid");
        }

        payroll.setPaidDate(LocalDate.now());
        return payrollRepository.save(payroll);
    }
}
