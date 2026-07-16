package com.employeemgmt.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "payroll",
       uniqueConstraints = @UniqueConstraint(columnNames = {"emp_id", "pay_month", "pay_year"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payroll {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pay_id")
    private Integer payId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emp_id", nullable = false, referencedColumnName = "emp_id")
    private Employee employee;

    @Column(name = "pay_month", nullable = false)
    private Integer payMonth;

    @Column(name = "pay_year", nullable = false)
    private Integer payYear;

    @Column(name = "basic_salary", nullable = false, precision = 12, scale = 2)
    private BigDecimal basicSalary;

    @Column(name = "bonus", precision = 12, scale = 2)
    @Builder.Default
    private BigDecimal bonus = BigDecimal.ZERO;

    @Column(name = "deductions", precision = 12, scale = 2)
    @Builder.Default
    private BigDecimal deductions = BigDecimal.ZERO;

    @Column(name = "net_salary", insertable = false, updatable = false, precision = 12, scale = 2)
    private BigDecimal netSalary;

    @Column(name = "paid_date")
    private LocalDate paidDate;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
