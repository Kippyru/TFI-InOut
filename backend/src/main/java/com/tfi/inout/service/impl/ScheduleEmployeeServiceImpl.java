package com.tfi.inout.service.impl;

import com.tfi.inout.dto.request.ScheduleEmployeeRequestDto;
import com.tfi.inout.dto.response.ScheduleEmployeeResponseDto;
import com.tfi.inout.mapper.ScheduleEmployeeMapper;
import com.tfi.inout.model.ScheduleEmployee;
import com.tfi.inout.repository.ScheduleEmployeeRepository;
import com.tfi.inout.service.ScheduleEmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleEmployeeServiceImpl implements ScheduleEmployeeService {

    private final ScheduleEmployeeRepository repository;
    private final ScheduleEmployeeMapper mapper;

    @Override
    @Transactional
    public ScheduleEmployeeResponseDto assignSchedule(ScheduleEmployeeRequestDto dto) {
        ScheduleEmployee se = mapper.toEntity(dto);
        return mapper.toDto(repository.save(se));
    }

    @Override
    public Optional<ScheduleEmployeeResponseDto> getActiveScheduleForEmployee(Long employeeId) {
        return repository.findActiveScheduleByEmployeeAndDate(employeeId, LocalDate.now())
                .map(mapper::toDto);
    }

    @Override
    public List<ScheduleEmployeeResponseDto> getAllSchedulesForEmployee(Long employeeId) {
        return repository.findByEmployeeId(employeeId).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }
}
