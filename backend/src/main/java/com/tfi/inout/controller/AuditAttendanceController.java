package com.tfi.inout.controller;

import com.tfi.inout.dto.AuditAttendanceDto;
import com.tfi.inout.service.AuditAttendanceService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/attendance")
@RequiredArgsConstructor
@Validated
public class AuditAttendanceController {
    private final AuditAttendanceService auditAttendanceService;

    @PostMapping("/audit")
    public ResponseEntity<AuditAttendanceDto> auditAttendance(
            @RequestParam @NotNull(message = "El adminId es obligatorio") Long adminId,
            @RequestParam @NotNull(message = "El eventAttendanceId es obligatorio") Long eventAttendanceId,
            @RequestParam @NotBlank(message = "La razon es obligatoria") String reason,
            @RequestParam @NotBlank(message = "El newValue es obligatorio") String newValue) {
        return ResponseEntity.ok(auditAttendanceService.auditAttendance(adminId, eventAttendanceId, reason, newValue));
    }
}
