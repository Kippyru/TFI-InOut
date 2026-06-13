package com.tfi.inout.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DailyAttendanceDto {
    private Long employeeId;
    private String numberEmployee;
    private String employeeName;
    private String employeeLastName;
    private String scheduleName;
    private LocalTime checkInTime;
    private LocalTime checkOutTime;
    private Long checkInEventId;
    private Long checkOutEventId;
    private String reason;
    private Instant date;
}
