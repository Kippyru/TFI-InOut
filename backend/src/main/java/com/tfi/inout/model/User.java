package com.tfi.inout.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Setter
@Entity
@Table(name = "user", indexes = @Index(columnList = "active"))
@SQLRestriction("active = true")
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "username", nullable = false, length = 50)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @ManyToOne(fetch = FetchType.EAGER) //SI O SI TIENE QUE SER EAGER EL ROL
    @JoinColumn(name = "role_id")
    private Role role;
}