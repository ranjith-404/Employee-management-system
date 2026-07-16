package com.employeemgmt.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "attendance",
       uniqueConstraints = @UniqueConstraint(columnNames = {"emp_id", "att_date"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Attendance {

    public enum Status {
        PRESENT, ABSENT, HALF_DAY, ON_LEAVE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "att_id")
    private Integer attId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emp_id", nullable = false, referencedColumnName = "emp_id")
    private Employee employee;

    @Column(name = "att_date", nullable = false)
    private LocalDate attDate;

    @Column(name = "check_in")
    private LocalTime checkIn;

    @Column(name = "check_out")
    private LocalTime checkOut;

    @Column(name = "hours_worked", precision = 4, scale = 2)
    private BigDecimal hoursWorked;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    @Builder.Default
    private Status status = Status.PRESENT;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
