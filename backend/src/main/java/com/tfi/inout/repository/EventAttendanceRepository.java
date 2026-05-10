package com.tfi.inout.repository;

import com.tfi.inout.model.EventAttendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventAttendanceRepository extends JpaRepository<EventAttendance, Long> {
    Optional<EventAttendance>
    findTopByEmployeeIdOrderByHourDesc(Long employeeId);
}