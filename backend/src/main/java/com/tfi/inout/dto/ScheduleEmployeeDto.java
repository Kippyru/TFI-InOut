package com.tfi.inout.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleEmployeeDto {
    private Long id;
    private Long employeeId;
    private Long scheduleId;
    private LocalDate startDate;
    private LocalDate endDate;
}
