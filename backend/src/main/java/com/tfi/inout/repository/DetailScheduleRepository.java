package com.tfi.inout.repository;

import com.tfi.inout.model.DetailSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DetailScheduleRepository extends JpaRepository<DetailSchedule, Long> {
    Optional<DetailSchedule> findByScheduleIdAndDayIgnoreCase(Long scheduleId, String day);
    List<DetailSchedule> findByScheduleId(Long scheduleId);
}
