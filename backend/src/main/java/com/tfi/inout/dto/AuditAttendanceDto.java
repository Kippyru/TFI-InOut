package com.tfi.inout.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuditAttendanceDto {
    private Long id;
    private Long eventAttendanceId;
    private Long adminId;
    private String previousValue;
    private String newValue;
    private Instant date;
    private String reason;
}
