package com.tfi.inout.controller;

import com.tfi.inout.dto.EventAttendanceDto;
import com.tfi.inout.service.EventAttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
public class EventAttendanceController {

    private final EventAttendanceService service;

    @PostMapping("/{employeeId}")
    public ResponseEntity<EventAttendanceDto> register(
            @PathVariable Long employeeId,
            @RequestParam String device
    ) {

        return ResponseEntity.ok(
                service.registerAttendance(employeeId, device)
        );
    }
}
