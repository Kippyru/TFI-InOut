package com.tfi.inout.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleDto {
    private Long id;

    @NotBlank(message = "El nombre del horario no puede estar vacío")
    private String name;

    private Boolean active;

    @NotNull(message = "Las horas de trabajo son obligatorias")
    private Integer hourWork;

    @NotNull(message = "La tolerancia de entrada es obligatoria")
    private Integer checkInTolerance;

    @NotNull(message = "La tolerancia de salida es obligatoria")
    private Integer checkOutTolerance;
}
