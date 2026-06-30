package com.tfi.inout.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.tfi.inout.dto.response.EventAttendanceResponseDto;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceStatusDto {

    private String nextEvent;
    private EventAttendanceResponseDto lastEvent;
    private boolean hasSchedule;
    private LocalTime scheduledCheckIn;
    private LocalTime scheduledCheckOut;
    private Integer checkInTolerance;
    private Integer checkOutTolerance;
}
