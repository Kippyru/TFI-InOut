package com.tfi.inout.repository;

import com.tfi.inout.model.ScheduleEmployee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface ScheduleEmployeeRepository extends JpaRepository<ScheduleEmployee, Long> {

    @Query("SELECT se FROM ScheduleEmployee se WHERE se.employee.id = :employeeId AND se.startDate <= :date AND (se.endDate IS NULL OR se.endDate >= :date)")
    Optional<ScheduleEmployee> findActiveScheduleByEmployeeAndDate(@Param("employeeId") Long employeeId, @Param("date") LocalDate date);

    @Query("SELECT se FROM ScheduleEmployee se WHERE se.employee.id = :employeeId ORDER BY se.startDate DESC")
    java.util.List<ScheduleEmployee> findByEmployeeId(@Param("employeeId") Long employeeId);
}
