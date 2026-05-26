package com.tfi.inout.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "schedule", indexes = @Index(columnList = "active"))
public class Schedule extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "state", length = 20)
    private String state;

    @Column(name = "hour_work")
    private Integer hourWork;

    @Column(name = "check_in_tolerance")
    private Integer checkInTolerance;

    @Column(name = "check_out_tolerance")
    private Integer checkOutTolerance;


}