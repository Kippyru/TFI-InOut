package com.tfi.inout.controller;

import com.tfi.inout.dto.request.DetailScheduleRequestDto;
import com.tfi.inout.dto.request.ScheduleRequestDto;
import com.tfi.inout.dto.request.ScheduleEmployeeRequestDto;
import com.tfi.inout.dto.response.DetailScheduleResponseDto;
import com.tfi.inout.dto.response.ScheduleResponseDto;
import com.tfi.inout.dto.response.ScheduleEmployeeResponseDto;
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
    public ResponseEntity<ScheduleResponseDto> createSchedule(@Valid @RequestBody ScheduleRequestDto dto) {
        return ResponseEntity.ok(scheduleService.createSchedule(dto));
    }

    @GetMapping
    public ResponseEntity<List<ScheduleResponseDto>> listSchedules() {
        return ResponseEntity.ok(scheduleService.listSchedules());
    }

    @GetMapping("/list/{id}")
    public ScheduleResponseDto getSchedule(@PathVariable Long id) { return scheduleService.listId(id);}

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateSchedule(@PathVariable Long id, @Valid @RequestBody ScheduleRequestDto dto) {
        scheduleService.edit(id, dto);
        return ResponseEntity.ok("Schedule updated");
    }

    @PutMapping("/restore/{id}")
    public ResponseEntity<String> restoreSchedule(@PathVariable Long id) {
        scheduleService.restore(id);
        return ResponseEntity.ok("Schedule successfully restored");
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<List<DetailScheduleResponseDto>> getDetails(@PathVariable Long id) {
        return ResponseEntity.ok(detailScheduleService.getDetailsBySchedule(id));
    }

    @PostMapping("/{id}/details")
    public ResponseEntity<DetailScheduleResponseDto> addDetail(@PathVariable Long id, @Valid @RequestBody DetailScheduleRequestDto dto) {
        return ResponseEntity.ok(detailScheduleService.addDetailToSchedule(id, dto));
    }

    @PostMapping("/{id}/details/bulk")
    public ResponseEntity<List<DetailScheduleResponseDto>> addDetailsBulk(@PathVariable Long id, @Valid @RequestBody List<DetailScheduleRequestDto> dtos) {
        return ResponseEntity.ok(detailScheduleService.createBulk(id, dtos));
    }

    @PostMapping("/assign")
    public ResponseEntity<ScheduleEmployeeResponseDto> assignSchedule(@Valid @RequestBody ScheduleEmployeeRequestDto dto) {
        return ResponseEntity.ok(scheduleEmployeeService.assignSchedule(dto));
    }

    @GetMapping("/employee/{employeeId}/active")
    public ResponseEntity<ScheduleEmployeeResponseDto> getActiveScheduleForEmployee(@PathVariable Long employeeId) {
        Optional<ScheduleEmployeeResponseDto> result = scheduleEmployeeService.getActiveScheduleForEmployee(employeeId);
        return result.map(ResponseEntity::ok).orElse(ResponseEntity.noContent().build());
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<ScheduleEmployeeResponseDto>> getAllSchedulesForEmployee(@PathVariable Long employeeId) {
        return ResponseEntity.ok(scheduleEmployeeService.getAllSchedulesForEmployee(employeeId));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteSchedule(@PathVariable Long id) {
        scheduleService.delete(id);
        return ResponseEntity.ok("Schedule successfully deleted");
    }
}
