package com.tfi.inout.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SoftDelete;
import org.hibernate.annotations.SoftDeleteType;

@MappedSuperclass
@Getter
@Setter
@SoftDelete(columnName = "active", strategy = SoftDeleteType.ACTIVE)
public abstract class BaseEntity {
}
