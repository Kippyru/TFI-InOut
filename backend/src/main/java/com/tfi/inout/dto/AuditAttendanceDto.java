package com.tfi.inout.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuditAttendanceDto {
    private Long id;

    @NotNull(message = "El evento de asistencia es obligatorio")
    private Long eventAttendanceId;

    @NotNull(message = "El ID del administrador es obligatorio")
    private Long adminId;

    private String previousValue;

    @NotBlank(message = "El nuevo valor es obligatorio")
    private String newValue;

    private Instant date;

    @NotBlank(message = "El motivo es obligatorio")
    private String reason;
}
