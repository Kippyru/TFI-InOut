package com.tfi.inout.service;

import com.tfi.inout.dto.ScheduleEmployeeDto;
import com.tfi.inout.mapper.ScheduleEmployeeMapper;
import com.tfi.inout.model.ScheduleEmployee;
import com.tfi.inout.repository.ScheduleEmployeeRepository;
import com.tfi.inout.handler.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

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

    public Optional<ScheduleEmployeeDto> getActiveScheduleForEmployee(Long employeeId) {
        return repository.findActiveScheduleByEmployeeAndDate(employeeId, LocalDate.now())
                .map(mapper::toDto);
    }

    public java.util.List<ScheduleEmployeeDto> getAllSchedulesForEmployee(Long employeeId) {
        return repository.findByEmployeeId(employeeId).stream()
                .map(mapper::toDto)
                .collect(java.util.stream.Collectors.toList());
    }
}
