package com.tfi.inout.service;

import com.tfi.inout.dto.DetailScheduleDto;
import com.tfi.inout.mapper.DetailScheduleMapper;
import com.tfi.inout.model.DetailSchedule;
import com.tfi.inout.model.Schedule;
import com.tfi.inout.repository.DetailScheduleRepository;
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
public class DetailScheduleService {
    private final DetailScheduleRepository detailScheduleRepository;
    private final ScheduleRepository scheduleRepository;
    private final DetailScheduleMapper detailScheduleMapper;

    public List<DetailScheduleDto> getDetailsBySchedule(Long scheduleId) {
        return detailScheduleRepository.findByScheduleId(scheduleId).stream()
                .map(detailScheduleMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public DetailScheduleDto addDetailToSchedule(Long scheduleId, DetailScheduleDto detailDto) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found"));
        detailDto.setScheduleId(scheduleId);
        DetailSchedule detail = detailScheduleMapper.toEntity(detailDto);
        return detailScheduleMapper.toDto(detailScheduleRepository.save(detail));
    }

    @Transactional
    public List<DetailScheduleDto> createBulk(Long scheduleId, List<DetailScheduleDto> dtos) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found"));
        
        List<DetailSchedule> details = dtos.stream().map(dto -> {
            dto.setScheduleId(scheduleId);
            return detailScheduleMapper.toEntity(dto);
        }).collect(Collectors.toList());

        return detailScheduleRepository.saveAll(details).stream()
                .map(detailScheduleMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public DetailScheduleDto update(Long id, DetailScheduleDto dto) {
        DetailSchedule detail = detailScheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DetailSchedule not found"));
        
        detailScheduleMapper.updateDetailSchedule(dto, detail);
        return detailScheduleMapper.toDto(detailScheduleRepository.save(detail));
    }

    @Transactional
    public void delete(Long id) {
        DetailSchedule detail = detailScheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DetailSchedule not found"));
        
        detail.setActive(false);
        detail.setDeletedAt(LocalDateTime.now());
        detailScheduleRepository.save(detail);
    }
}
