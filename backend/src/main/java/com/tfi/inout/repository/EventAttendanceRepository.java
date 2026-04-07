package com.tfi.inout.repository;

import com.tfi.inout.model.EventAttendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventAttendanceRepository extends JpaRepository<EventAttendance, Long> {
}