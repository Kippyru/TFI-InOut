package com.tfi.inout.controller;

import com.tfi.inout.dto.DetailScheduleDto;
import com.tfi.inout.dto.ScheduleDto;
import com.tfi.inout.dto.ScheduleEmployeeDto;
import com.tfi.inout.service.ScheduleEmployeeService;
import com.tfi.inout.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleService scheduleService;
    private final ScheduleEmployeeService scheduleEmployeeService;

    @PostMapping
    public ResponseEntity<ScheduleDto> createSchedule(@Valid @RequestBody ScheduleDto dto) {
        return ResponseEntity.ok(scheduleService.createSchedule(dto));
    }

    @GetMapping
    public ResponseEntity<List<ScheduleDto>> listSchedules() {
        return ResponseEntity.ok(scheduleService.listSchedules());
    }

    @PutMapping("/restore/{id}")
    public ResponseEntity<String> restoreSchedule(@PathVariable Long id) {
        scheduleService.restore(id);
        return ResponseEntity.ok("Schedule successfully restored");
    }

    @PostMapping("/{id}/details")
    public ResponseEntity<DetailScheduleDto> addDetail(@PathVariable Long id, @Valid @RequestBody DetailScheduleDto dto) {
        return ResponseEntity.ok(scheduleService.addDetailToSchedule(id, dto));
    }

    @PostMapping("/assign")
    public ResponseEntity<ScheduleEmployeeDto> assignSchedule(@Valid @RequestBody ScheduleEmployeeDto dto) {
        return ResponseEntity.ok(scheduleEmployeeService.assignSchedule(dto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteSchedule(@PathVariable Long id) {
        scheduleService.delete(id);
        return ResponseEntity.ok("Schedule successfully deleted");
    }
}
