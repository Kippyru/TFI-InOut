package com.tfi.inout.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Entity
@Table(name = "event_attendance", indexes = @Index(columnList = "active"))
public class EventAttendance extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @Column(name = "event_type", length = 50)
    private String eventType;

    @Column(name = "hour")
    private LocalTime hour;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "device", length = 100)
    private String device;

    @Column(name = "state", length = 20)
    private String state;


}