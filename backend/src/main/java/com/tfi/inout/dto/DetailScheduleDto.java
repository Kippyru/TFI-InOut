package com.tfi.inout.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetailScheduleDto {
    private Long id;
    private Long scheduleId;
    private String day;
    private LocalTime checkIn;
    private LocalTime checkOut;
}
