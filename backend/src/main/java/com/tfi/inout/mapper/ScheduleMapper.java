package com.tfi.inout.mapper;

import com.tfi.inout.dto.ScheduleDto;
import com.tfi.inout.model.Schedule;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ScheduleMapper {
    Schedule toEntity(ScheduleDto dto);
    ScheduleDto toDto(Schedule entity);
}
