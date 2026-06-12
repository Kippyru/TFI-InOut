package com.tfi.inout.service;

import com.tfi.inout.dto.ScheduleDto;
import com.tfi.inout.mapper.ScheduleMapper;
import com.tfi.inout.model.Employee;
import com.tfi.inout.model.Schedule;
import com.tfi.inout.model.Schedule;
import com.tfi.inout.repository.ScheduleRepository;
import com.tfi.inout.handler.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final ScheduleMapper scheduleMapper;

    @Transactional
    public ScheduleDto createSchedule(ScheduleDto scheduleDto) {
        Schedule schedule = scheduleMapper.toEntity(scheduleDto);
        return scheduleMapper.toDto(scheduleRepository.save(schedule));
    }

    @Transactional
    public void restore(Long id) {
        scheduleRepository.restoreById(id);
    }

    public List<ScheduleDto> listSchedules() {
        return scheduleRepository.findAll().stream().map(scheduleMapper::toDto).collect(Collectors.toList());
    }

    public ScheduleDto listId(Long id) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found"));
        return scheduleMapper.toDto(schedule);
    }

    public ScheduleDto edit(Long id, ScheduleDto scheduleDto) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found"));
        scheduleMapper.updateSchedule(scheduleDto, schedule);
        Schedule updateSchedule = scheduleRepository.save(schedule);
        return scheduleMapper.toDto(updateSchedule);
    }

    @Transactional
    public void delete(Long id) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found"));

        schedule.setActive(false);
        schedule.setDeletedAt(LocalDateTime.now());

        scheduleRepository.save(schedule);
    }
}
