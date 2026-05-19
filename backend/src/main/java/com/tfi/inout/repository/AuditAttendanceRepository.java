package com.tfi.inout.repository;

import com.tfi.inout.model.AuditAttendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditAttendanceRepository extends JpaRepository<AuditAttendance, Long> {
}
