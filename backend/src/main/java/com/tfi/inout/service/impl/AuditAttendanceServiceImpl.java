package com.tfi.inout.service.impl;

import com.tfi.inout.dto.DailyAttendanceDto;
import com.tfi.inout.dto.response.AuditAttendanceResponseDto;
import com.tfi.inout.handler.ResourceNotFoundException;
import com.tfi.inout.mapper.AuditAttendanceMapper;
import com.tfi.inout.model.*;
import com.tfi.inout.repository.*;
import com.tfi.inout.service.AuditAttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuditAttendanceServiceImpl implements AuditAttendanceService {

    private final AuditAttendanceRepository auditRepository;
    private final EventAttendanceRepository eventRepository;
    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final ScheduleEmployeeRepository scheduleEmployeeRepository;
    private final AuditAttendanceMapper mapper;

    @Override
    @Transactional
    public AuditAttendanceResponseDto auditAttendance(Long adminId, Long eventAttendanceId, String reason, String newValue) {
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found"));
        EventAttendance event = eventRepository.findById(eventAttendanceId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));

        String previousValue = event.getHour().toString();

        AuditAttendance audit = new AuditAttendance();
        audit.setAdmin(admin);
        audit.setEventAttendance(event);
        audit.setPreviousValue(previousValue);
        audit.setNewValue(newValue);
        audit.setDate(Instant.now());
        audit.setReason(reason);

        event.setHour(LocalTime.parse(newValue));
        eventRepository.save(event);

        return mapper.toDto(auditRepository.save(audit));
    }

    @Override
    @Transactional(readOnly = true)
    public List<DailyAttendanceDto> getDailyAttendance(LocalDate date) {
        List<Employee> employees = employeeRepository.findByActiveTrue();
        List<EventAttendance> attendances = eventRepository.findByDate(date);
        List<ScheduleEmployee> schedules = scheduleEmployeeRepository.findActiveSchedulesByDate(date);

        Map<Long, List<EventAttendance>> attendanceMap = attendances.stream()
                .collect(Collectors.groupingBy(a -> a.getEmployee().getId()));

        Map<Long, ScheduleEmployee> scheduleMap = schedules.stream()
                .collect(Collectors.toMap(
                        se -> se.getEmployee().getId(),
                        Function.identity(),
                        (first, second) -> first
                ));

        List<Long> attendanceIds = attendances.stream()
                .map(EventAttendance::getId)
                .toList();

        List<AuditAttendance> audits = attendanceIds.isEmpty()
                ? Collections.emptyList()
                : auditRepository.findByEventAttendanceIds(attendanceIds);

        Map<Long, AuditAttendance> auditMap = audits.stream()
                .collect(Collectors.toMap(
                        a -> a.getEventAttendance().getId(),
                        Function.identity(),
                        (a1, a2) -> a1.getDate().isAfter(a2.getDate()) ? a1 : a2
                ));

        List<DailyAttendanceDto> result = new ArrayList<>();

        for (Employee employee : employees) {
            DailyAttendanceDto dto = new DailyAttendanceDto();
            dto.setEmployeeId(employee.getId());
            dto.setNumberEmployee(employee.getNumberEmployee());
            dto.setEmployeeName(employee.getName());
            dto.setEmployeeLastName(employee.getLastName());

            ScheduleEmployee schedule = scheduleMap.get(employee.getId());
            dto.setScheduleName(schedule != null ? schedule.getSchedule().getName() : "Sin turno");

            List<EventAttendance> employeeEvents = attendanceMap.getOrDefault(employee.getId(), Collections.emptyList());

            Optional<EventAttendance> checkIn = employeeEvents.stream()
                    .filter(e -> "CHECK_IN".equals(e.getEventType()))
                    .min(Comparator.comparing(EventAttendance::getHour));

            Optional<EventAttendance> checkOut = employeeEvents.stream()
                    .filter(e -> "CHECK_OUT".equals(e.getEventType()))
                    .max(Comparator.comparing(EventAttendance::getHour));

            checkIn.ifPresent(e -> {
                dto.setCheckInTime(e.getHour());
                dto.setCheckInEventId(e.getId());
                AuditAttendance audit = auditMap.get(e.getId());
                if (audit != null) {
                    dto.setReason(audit.getReason());
                }
            });

            checkOut.ifPresent(e -> {
                dto.setCheckOutTime(e.getHour());
                dto.setCheckOutEventId(e.getId());
                AuditAttendance audit = auditMap.get(e.getId());
                if (audit != null) {
                    dto.setReason(audit.getReason());
                    dto.setDate(audit.getDate());
                }
            });

            result.add(dto);
        }

        return result;
    }
}
