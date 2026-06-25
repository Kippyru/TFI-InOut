package com.tfi.inout.mapper;

import com.tfi.inout.dto.request.DetailScheduleRequestDto;
import com.tfi.inout.dto.response.DetailScheduleResponseDto;
import com.tfi.inout.model.DetailSchedule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface DetailScheduleMapper {

    @Mapping(source = "scheduleId", target = "schedule.id")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    DetailSchedule toEntity(DetailScheduleRequestDto dto);

    @Mapping(source = "schedule.id", target = "scheduleId")
    DetailScheduleResponseDto toDto(DetailSchedule entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(source = "scheduleId", target = "schedule.id")
    void updateDetailSchedule(DetailScheduleRequestDto dto, @MappingTarget DetailSchedule entity);
}
