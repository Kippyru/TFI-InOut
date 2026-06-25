package com.tfi.inout.service.impl;

import com.tfi.inout.dto.request.DetailScheduleRequestDto;
import com.tfi.inout.dto.response.DetailScheduleResponseDto;
import com.tfi.inout.handler.ResourceNotFoundException;
import com.tfi.inout.mapper.DetailScheduleMapper;
import com.tfi.inout.model.DetailSchedule;
import com.tfi.inout.model.Schedule;
import com.tfi.inout.repository.DetailScheduleRepository;
import com.tfi.inout.repository.ScheduleRepository;
import com.tfi.inout.service.DetailScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DetailScheduleServiceImpl implements DetailScheduleService {

    private final DetailScheduleRepository detailScheduleRepository;
    private final ScheduleRepository scheduleRepository;
    private final DetailScheduleMapper detailScheduleMapper;

    @Override
    public List<DetailScheduleResponseDto> getDetailsBySchedule(Long scheduleId) {
        return detailScheduleRepository.findByScheduleId(scheduleId).stream()
                .map(detailScheduleMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public DetailScheduleResponseDto addDetailToSchedule(Long scheduleId, DetailScheduleRequestDto detailDto) {
        scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found"));
        detailDto.setScheduleId(scheduleId);
        DetailSchedule detail = detailScheduleMapper.toEntity(detailDto);
        return detailScheduleMapper.toDto(detailScheduleRepository.save(detail));
    }

    @Override
    @Transactional
    public List<DetailScheduleResponseDto> createBulk(Long scheduleId, List<DetailScheduleRequestDto> dtos) {
        scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found"));

        List<DetailSchedule> details = dtos.stream().map(dto -> {
            dto.setScheduleId(scheduleId);
            return detailScheduleMapper.toEntity(dto);
        }).collect(Collectors.toList());

        return detailScheduleRepository.saveAll(details).stream()
                .map(detailScheduleMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public DetailScheduleResponseDto update(Long id, DetailScheduleRequestDto dto) {
        DetailSchedule detail = detailScheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DetailSchedule not found"));
        detailScheduleMapper.updateDetailSchedule(dto, detail);
        return detailScheduleMapper.toDto(detailScheduleRepository.save(detail));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        DetailSchedule detail = detailScheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DetailSchedule not found"));
        detail.setActive(false);
        detail.setDeletedAt(LocalDateTime.now());
        detailScheduleRepository.save(detail);
    }
}
