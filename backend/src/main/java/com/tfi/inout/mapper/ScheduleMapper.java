package com.tfi.inout.mapper;

import com.tfi.inout.dto.request.ScheduleRequestDto;
import com.tfi.inout.dto.response.ScheduleResponseDto;
import com.tfi.inout.model.Schedule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ScheduleMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    Schedule toEntity(ScheduleRequestDto dto);

    ScheduleResponseDto toDto(Schedule entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    void updateSchedule(ScheduleRequestDto scheduleDto, @MappingTarget Schedule schedule);
}
