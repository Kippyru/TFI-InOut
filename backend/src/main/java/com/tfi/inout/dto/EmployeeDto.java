package com.tfi.inout.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDto {
    private Long id;
    private String name;
    private String lastName;
    private String numberEmployee;
    private String cuil;
    private String dni;
    private String state;
    private LocalDate dateEntry;
    private Long user;
}
