package com.tfi.inout.repository;

import com.tfi.inout.model.AuditAttendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuditAttendanceRepository extends JpaRepository<AuditAttendance, Long> {

    @Query("""
    SELECT a
    FROM AuditAttendance a
    WHERE a.eventAttendance.id IN :eventIds
""")
    List<AuditAttendance> findByEventAttendanceIds(
            @Param("eventIds") List<Long> eventIds);
}
