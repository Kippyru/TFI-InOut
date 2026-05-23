package com.tfi.inout.controller;

import com.tfi.inout.dto.EventAttendanceDto;
import com.tfi.inout.service.EventAttendanceService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
@Validated
public class EventAttendanceController {

    private final EventAttendanceService service;

    @PostMapping("/{employeeId}")
    public ResponseEntity<EventAttendanceDto> register(
            @PathVariable Long employeeId,
            @RequestParam @NotBlank(message = "El dispositivo es obligatorio") String device
    ) {

        return ResponseEntity.ok(
                service.registerAttendance(employeeId, device)
        );
    }
}
