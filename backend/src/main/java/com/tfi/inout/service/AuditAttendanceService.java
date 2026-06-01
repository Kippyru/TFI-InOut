package com.tfi.inout.service;

import com.tfi.inout.dto.AuditAttendanceDto;
import com.tfi.inout.mapper.AuditAttendanceMapper;
import com.tfi.inout.model.AuditAttendance;
import com.tfi.inout.model.EventAttendance;
import com.tfi.inout.model.User;
import com.tfi.inout.repository.AuditAttendanceRepository;
import com.tfi.inout.repository.EventAttendanceRepository;
import com.tfi.inout.repository.UserRepository;
import com.tfi.inout.handler.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class AuditAttendanceService {
    private final AuditAttendanceRepository auditRepository;
    private final EventAttendanceRepository eventRepository;
    private final UserRepository userRepository;
    private final AuditAttendanceMapper mapper;

    @Transactional
    public AuditAttendanceDto auditAttendance(Long adminId, Long eventAttendanceId, String reason, String newValue) {
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found"));
        EventAttendance event = eventRepository.findById(eventAttendanceId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));

        String previousValue = event.getHour().toString();
        
        AuditAttendance audit = new AuditAttendance();
        audit.setAdmin(admin);
        audit.setEvent_attendance(event);
        audit.setPreviousValue(previousValue);
        audit.setNewValue(newValue);
        audit.setDate(Instant.now());
        audit.setReason(reason);

        event.setHour(LocalTime.parse(newValue));
        eventRepository.save(event);

        return mapper.toDto(auditRepository.save(audit));
    }
}
