package com.tfi.inout.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventAttendanceDto {
    private Long id;
    private Long employee;
    private String eventType;
    private LocalTime hour;
    private LocalDate date;
    private String device;
    private String state;
}
