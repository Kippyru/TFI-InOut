package com.tfi.inout.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestDto {
    @NotBlank(message = "Username no puede estar vacío")
    private String username;
    
    @NotBlank(message = "Password no puede estar vacío")
    private String password;
}
