package com.tfi.inout.controller;

import com.tfi.inout.dto.AttendanceStatusDto;
import com.tfi.inout.dto.response.EventAttendanceResponseDto;
import com.tfi.inout.service.EventAttendanceService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
@Validated
public class EventAttendanceController {

    private final EventAttendanceService service;

    @GetMapping("/{employeeId}/status")
    public ResponseEntity<AttendanceStatusDto> getStatus(@PathVariable Long employeeId) {
        return ResponseEntity.ok(service.getAttendanceStatus(employeeId));
    }

    @PostMapping("/{employeeId}")
    public ResponseEntity<EventAttendanceResponseDto> register(
            @PathVariable Long employeeId,
            @RequestParam @NotBlank(message = "El dispositivo es obligatorio") String device
    ) {

        return ResponseEntity.ok(
                service.registerAttendance(employeeId, device)
        );
    }

    @GetMapping("/list")
    public ResponseEntity<List<EventAttendanceResponseDto>> list(
            @RequestParam(required = false) LocalDate date) {
        return ResponseEntity.ok(service.list(date));
    }
}
