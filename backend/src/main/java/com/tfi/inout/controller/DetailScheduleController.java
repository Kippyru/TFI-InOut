package com.tfi.inout.controller;

import com.tfi.inout.dto.request.DetailScheduleRequestDto;
import com.tfi.inout.dto.response.DetailScheduleResponseDto;
import com.tfi.inout.service.DetailScheduleService;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/detail-schedules")
@RequiredArgsConstructor
public class DetailScheduleController {
    private final DetailScheduleService detailScheduleService;

    @PutMapping("/{id}")
    public ResponseEntity<DetailScheduleResponseDto> updateDetail(@PathVariable Long id, @Valid @RequestBody DetailScheduleRequestDto dto) {
        return ResponseEntity.ok(detailScheduleService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDetail(@PathVariable Long id) {
        detailScheduleService.delete(id);
        return ResponseEntity.ok("DetailSchedule successfully deleted");
    }
}
