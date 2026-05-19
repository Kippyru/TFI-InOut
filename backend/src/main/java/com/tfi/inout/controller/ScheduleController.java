package com.tfi.inout.controller;

import com.tfi.inout.dto.DetailScheduleDto;
import com.tfi.inout.dto.ScheduleDto;
import com.tfi.inout.dto.ScheduleEmployeeDto;
import com.tfi.inout.service.ScheduleEmployeeService;
import com.tfi.inout.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleService scheduleService;
    private final ScheduleEmployeeService scheduleEmployeeService;

    @PostMapping
    public ResponseEntity<ScheduleDto> createSchedule(@RequestBody ScheduleDto dto) {
        return ResponseEntity.ok(scheduleService.createSchedule(dto));
    }

    @GetMapping
    public ResponseEntity<List<ScheduleDto>> listSchedules() {
        return ResponseEntity.ok(scheduleService.listSchedules());
    }

    @PostMapping("/{id}/details")
    public ResponseEntity<DetailScheduleDto> addDetail(@PathVariable Long id, @RequestBody DetailScheduleDto dto) {
        return ResponseEntity.ok(scheduleService.addDetailToSchedule(id, dto));
    }

    @PostMapping("/assign")
    public ResponseEntity<ScheduleEmployeeDto> assignSchedule(@RequestBody ScheduleEmployeeDto dto) {
        return ResponseEntity.ok(scheduleEmployeeService.assignSchedule(dto));
    }
}
