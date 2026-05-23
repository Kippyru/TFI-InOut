package com.tfi.inout.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetailScheduleDto {
    private Long id;

    @NotNull(message = "El ID del horario es obligatorio")
    private Long scheduleId;

    @NotBlank(message = "El día es obligatorio")
    private String day;

    @NotNull(message = "La hora de entrada es obligatoria")
    private LocalTime checkIn;

    @NotNull(message = "La hora de salida es obligatoria")
    private LocalTime checkOut;
}
