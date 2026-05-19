package com.tfi.inout.mapper;

import com.tfi.inout.dto.DetailScheduleDto;
import com.tfi.inout.model.DetailSchedule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DetailScheduleMapper {
    @Mapping(source = "scheduleId", target = "schedule.id")
    DetailSchedule toEntity(DetailScheduleDto dto);

    @Mapping(source = "schedule.id", target = "scheduleId")
    DetailScheduleDto toDto(DetailSchedule entity);
}
