package com.tfi.inout.controller;

import com.tfi.inout.dto.DetailScheduleDto;
import com.tfi.inout.dto.ScheduleDto;
import com.tfi.inout.dto.ScheduleEmployeeDto;
import com.tfi.inout.service.DetailScheduleService;
import com.tfi.inout.service.ScheduleEmployeeService;
import com.tfi.inout.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleService scheduleService;
    private final ScheduleEmployeeService scheduleEmployeeService;
    private final DetailScheduleService detailScheduleService;

    @PostMapping
    public ResponseEntity<ScheduleDto> createSchedule(@Valid @RequestBody ScheduleDto dto) {
        return ResponseEntity.ok(scheduleService.createSchedule(dto));
    }

    @GetMapping
    public ResponseEntity<List<ScheduleDto>> listSchedules() {
        return ResponseEntity.ok(scheduleService.listSchedules());
    }

    @GetMapping("/list/{id}")
    public ScheduleDto getSchedule(@PathVariable Long id) { return scheduleService.listId(id);}

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateSchedule(@PathVariable Long id, @Valid @RequestBody ScheduleDto dto) {
        scheduleService.edit(id, dto);
        return ResponseEntity.ok("Schedule updated");
    }

    @PutMapping("/restore/{id}")
    public ResponseEntity<String> restoreSchedule(@PathVariable Long id) {
        scheduleService.restore(id);
        return ResponseEntity.ok("Schedule successfully restored");
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<List<DetailScheduleDto>> getDetails(@PathVariable Long id) {
        return ResponseEntity.ok(detailScheduleService.getDetailsBySchedule(id));
    }

    @PostMapping("/{id}/details")
    public ResponseEntity<DetailScheduleDto> addDetail(@PathVariable Long id, @Valid @RequestBody DetailScheduleDto dto) {
        return ResponseEntity.ok(detailScheduleService.addDetailToSchedule(id, dto));
    }

    @PostMapping("/{id}/details/bulk")
    public ResponseEntity<List<DetailScheduleDto>> addDetailsBulk(@PathVariable Long id, @Valid @RequestBody List<DetailScheduleDto> dtos) {
        return ResponseEntity.ok(detailScheduleService.createBulk(id, dtos));
    }

    @PostMapping("/assign")
    public ResponseEntity<ScheduleEmployeeDto> assignSchedule(@Valid @RequestBody ScheduleEmployeeDto dto) {
        return ResponseEntity.ok(scheduleEmployeeService.assignSchedule(dto));
    }

    @GetMapping("/employee/{employeeId}/active")
    public ResponseEntity<ScheduleEmployeeDto> getActiveScheduleForEmployee(@PathVariable Long employeeId) {
        Optional<ScheduleEmployeeDto> result = scheduleEmployeeService.getActiveScheduleForEmployee(employeeId);
        return result.map(ResponseEntity::ok).orElse(ResponseEntity.noContent().build());
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<ScheduleEmployeeDto>> getAllSchedulesForEmployee(@PathVariable Long employeeId) {
        return ResponseEntity.ok(scheduleEmployeeService.getAllSchedulesForEmployee(employeeId));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteSchedule(@PathVariable Long id) {
        scheduleService.delete(id);
        return ResponseEntity.ok("Schedule successfully deleted");
    }
}
