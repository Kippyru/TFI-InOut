package com.tfi.inout.controller;

import com.tfi.inout.dto.AuditAttendanceDto;
import com.tfi.inout.dto.DailyAttendanceDto;
import com.tfi.inout.service.AuditAttendanceService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@CrossOrigin
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

    @GetMapping("/daily")
    public ResponseEntity<List<DailyAttendanceDto>> getDailyAttendance(
            @RequestParam(required = false) LocalDate date) {

        return ResponseEntity.ok(
                auditAttendanceService.getDailyAttendance(
                        date != null ? date : LocalDate.now()
                )
        );
    }
}
