package com.tfi.inout.service.impl;

import com.tfi.inout.dto.request.ScheduleRequestDto;
import com.tfi.inout.dto.response.ScheduleResponseDto;
import com.tfi.inout.handler.ResourceNotFoundException;
import com.tfi.inout.mapper.ScheduleMapper;
import com.tfi.inout.model.Schedule;
import com.tfi.inout.repository.ScheduleRepository;
import com.tfi.inout.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final ScheduleMapper scheduleMapper;

    @Override
    @Transactional
    public ScheduleResponseDto createSchedule(ScheduleRequestDto scheduleRequestDto) {
        Schedule schedule = scheduleMapper.toEntity(scheduleRequestDto);
        return scheduleMapper.toDto(scheduleRepository.save(schedule));
    }

    @Override
    public List<ScheduleResponseDto> listSchedules() {
        return scheduleRepository.findAll().stream()
                .map(scheduleMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ScheduleResponseDto listId(Long id) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found"));
        return scheduleMapper.toDto(schedule);
    }

    @Override
    public ScheduleResponseDto edit(Long id, ScheduleRequestDto scheduleRequestDto) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found"));
        scheduleMapper.updateSchedule(scheduleRequestDto, schedule);
        return scheduleMapper.toDto(scheduleRepository.save(schedule));
    }

    @Override
    @Transactional
    public void restore(Long id) {
        scheduleRepository.restoreById(id);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found"));
        schedule.setActive(false);
        schedule.setDeletedAt(LocalDateTime.now());
        scheduleRepository.save(schedule);
    }
}
