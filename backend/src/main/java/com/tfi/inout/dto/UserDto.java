package com.tfi.inout.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;

    @NotBlank(message = "El username no puede estar vacío")
    private String username;

    private String password;

    @NotNull(message = "El rol es obligatorio")
    private Long role;

    private Boolean active;
}
