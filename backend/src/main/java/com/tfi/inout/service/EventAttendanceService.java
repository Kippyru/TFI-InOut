package com.tfi.inout.service;

import com.tfi.inout.dto.EventAttendanceDto;
import com.tfi.inout.mapper.EventAttendanceMapper;
import com.tfi.inout.model.DetailSchedule;
import com.tfi.inout.model.Employee;
import com.tfi.inout.model.EventAttendance;
import com.tfi.inout.model.ScheduleEmployee;
import com.tfi.inout.repository.DetailScheduleRepository;
import com.tfi.inout.repository.EmployeeRepository;
import com.tfi.inout.repository.EventAttendanceRepository;
import com.tfi.inout.repository.ScheduleEmployeeRepository;
import com.tfi.inout.exception.ResourceNotFoundException;
import com.tfi.inout.exception.BusinessException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventAttendanceService {

    private final EventAttendanceRepository repository;
    private final EmployeeRepository employeeRepository;
    private final EventAttendanceMapper mapper;
    private final ScheduleEmployeeRepository scheduleEmployeeRepository;
    private final DetailScheduleRepository detailScheduleRepository;

    @Transactional
    public EventAttendanceDto registerAttendance(Long employeeId, String device) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        String nextEvent = determineEventType(employeeId, today, now);

        Optional<EventAttendance> existingEvent = repository.findByEmployeeIdAndDateAndEventType(employeeId, today, nextEvent);
        if (existingEvent.isPresent()) {
            throw new BusinessException("Already checked " + (nextEvent.equals("CHECK_IN") ? "in" : "out") + " for today");
        }

        EventAttendance attendance = new EventAttendance();
        attendance.setEmployee(employee);
        attendance.setEventType(nextEvent);
        attendance.setHour(now);
        attendance.setDate(today);
        attendance.setDevice(device);
        attendance.setState("ACTIVE");

        attendance = repository.save(attendance);

        return mapper.toDto(attendance);
    }

    private String determineEventType(Long employeeId, LocalDate date, LocalTime time) {
        Optional<ScheduleEmployee> scheduleEmployeeOpt = scheduleEmployeeRepository.findActiveScheduleByEmployeeAndDate(employeeId, date);
        if (scheduleEmployeeOpt.isPresent()) {
            String dayOfWeek = date.getDayOfWeek().name();
            Optional<DetailSchedule> detailOpt = detailScheduleRepository.findByScheduleIdAndDay(scheduleEmployeeOpt.get().getSchedule().getId(), dayOfWeek);
            if (detailOpt.isPresent()) {
                DetailSchedule detail = detailOpt.get();
                long diffIn = Math.abs(ChronoUnit.MINUTES.between(time, detail.getCheckIn()));
                long diffOut = Math.abs(ChronoUnit.MINUTES.between(time, detail.getCheckOut()));

                if (diffIn <= diffOut) {
                    return "CHECK_IN";
                } else {
                    return "CHECK_OUT";
                }
            }
        }
        
        Optional<EventAttendance> lastEventOpt = repository.findByEmployeeIdAndDate(employeeId, date).stream()
                .max((e1, e2) -> e1.getHour().compareTo(e2.getHour()));
        
        if (lastEventOpt.isEmpty()) {
            return "CHECK_IN";
        }
        return lastEventOpt.get().getEventType().equals("CHECK_IN") ? "CHECK_OUT" : "CHECK_IN";
    }
}