package com.tfi.inout.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleRequestDto {
    @NotBlank(message = "El nombre del horario no puede estar vacío")
    private String name;

    @NotNull(message = "Las horas de trabajo son obligatorias")
    private Integer hourWork;

    @NotNull(message = "La tolerancia de entrada es obligatoria")
    private Integer checkInTolerance;

    @NotNull(message = "La tolerancia de salida es obligatoria")
    private Integer checkOutTolerance;
}
