package com.tfi.inout.service;

import com.tfi.inout.dto.EventAttendanceDto;
import com.tfi.inout.mapper.EventAttendanceMapper;
import com.tfi.inout.model.Employee;
import com.tfi.inout.model.EventAttendance;
import com.tfi.inout.repository.EmployeeRepository;
import com.tfi.inout.repository.EventAttendanceRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventAttendanceService {

    private final EventAttendanceRepository repository;
    private final EmployeeRepository employeeRepository;
    private final EventAttendanceMapper mapper;

    @Transactional
    public EventAttendanceDto registerAttendance(Long employeeId, String device) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        Optional<EventAttendance> lastEventOpt =
                repository.findTopByEmployeeIdOrderByHourDesc(employeeId);

        String nextEvent;

        if (lastEventOpt.isEmpty()) {
            nextEvent = "CHECK_IN";
        } else {

            String lastType = lastEventOpt.get().getEventType();

            if ("CHECK_IN".equals(lastType)) {
                nextEvent = "CHECK_OUT";
            } else {
                nextEvent = "CHECK_IN";
            }
        }

        EventAttendance attendance = new EventAttendance();

        attendance.setEmployee(employee);
        attendance.setEventType(nextEvent);
        attendance.setHour(LocalTime.now());
        attendance.setDevice(device);
        attendance.setState("ACTIVE");

        attendance = repository.save(attendance);

        return mapper.toDto(attendance);
    }
}