package com.tfi.inout.service;

import com.tfi.inout.dto.ScheduleEmployeeDto;
import com.tfi.inout.mapper.ScheduleEmployeeMapper;
import com.tfi.inout.model.ScheduleEmployee;
import com.tfi.inout.repository.ScheduleEmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ScheduleEmployeeService {
    private final ScheduleEmployeeRepository repository;
    private final ScheduleEmployeeMapper mapper;

    @Transactional
    public ScheduleEmployeeDto assignSchedule(ScheduleEmployeeDto dto) {
        ScheduleEmployee se = mapper.toEntity(dto);
        return mapper.toDto(repository.save(se));
    }
}
