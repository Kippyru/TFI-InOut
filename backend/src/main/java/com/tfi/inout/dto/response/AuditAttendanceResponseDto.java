package com.tfi.inout.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuditAttendanceResponseDto {
    private Long id;
    private Long eventAttendanceId;
    private Long adminId;
    private String previousValue;
    private String newValue;
    private Instant date;
    private String reason;
}
