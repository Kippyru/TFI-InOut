package com.tfi.inout.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleResponseDto {
    private Long id;
    private String name;
    private Boolean active;
    private Integer hourWork;
    private Integer checkInTolerance;
    private Integer checkOutTolerance;
}
