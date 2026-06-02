package com.tfi.inout.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "employee", indexes = @Index(columnList = "active"))
@SQLRestriction("active = true")
public class Employee extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name = "number_employee", length = 50)
    private String numberEmployee;

    @Column(name = "cuil", length = 20)
    private String cuil;

    @Column(name = "dni", length = 20)
    private String dni;

    @Column(name = "state", length = 20)
    private String state;

    @Column(name = "date_entry")
    private LocalDateTime dateEntry;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;


}