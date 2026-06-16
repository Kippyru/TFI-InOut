package com.tfi.inout.repository;

import com.tfi.inout.model.EventAttendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventAttendanceRepository extends JpaRepository<EventAttendance, Long> {
    List<EventAttendance> findByEmployeeIdAndDate(Long employeeId, LocalDate date);

    Optional<EventAttendance> findByEmployeeIdAndDateAndEventType(Long employeeId, LocalDate date, String eventType);

    List<EventAttendance> findByDate(LocalDate date);
}