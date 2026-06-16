package com.tfi.inout.controller;

import com.tfi.inout.dto.dashboard.MetricsDTO;
import com.tfi.inout.service.MetricsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class MetricsController {

    private final MetricsService dashboardService;

    @GetMapping("/metrics")
    public ResponseEntity<MetricsDTO> getMetrics() {
        return ResponseEntity.ok(dashboardService.getDashboardMetrics());
    }
}
