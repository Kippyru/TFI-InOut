package com.tfi.inout.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "user", indexes = @Index(columnList = "active"))
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "username", nullable = false, length = 50)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @ManyToOne(fetch = FetchType.EAGER) //al usar soft delete no se puede usar LAZY, pero en este caso es mejor EAGER de todas formas
    @JoinColumn(name = "role_id")
    private Role role;

    @Column(name = "state", length = 20)
    private String state;


}