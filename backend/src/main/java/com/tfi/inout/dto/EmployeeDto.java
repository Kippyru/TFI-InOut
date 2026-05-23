package com.tfi.inout.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDto {
    private Long id;

    @NotBlank(message = "El nombre no puede estar vacío")
    private String name;

    @NotBlank(message = "El apellido no puede estar vacío")
    private String lastName;

    private String numberEmployee;
    private String cuil;
    private String dni;
    private String state;

    @NotNull(message = "La fecha de entrada es obligatoria")
    private LocalDate dateEntry;

    @NotNull(message = "El usuario asociado es obligatorio")
    private Long user;
}
