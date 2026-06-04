package com.tfi.inout.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
public abstract class BaseEntity {
    @Column(name = "active", nullable = false)
    private Boolean active = true;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
