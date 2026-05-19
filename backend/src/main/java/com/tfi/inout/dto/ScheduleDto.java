package com.tfi.inout.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleDto {
    private Long id;
    private String name;
    private String state;
    private Integer hourWork;
    private Integer checkInTolerance;
    private Integer checkOutTolerance;
}
