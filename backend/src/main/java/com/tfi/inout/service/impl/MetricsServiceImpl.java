package com.tfi.inout.service.impl;

import com.tfi.inout.dto.dashboard.*;
import com.tfi.inout.repository.MetricsRepository;
import com.tfi.inout.service.MetricsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MetricsServiceImpl implements MetricsService {

    private final MetricsRepository analyticsRepository;

    @Override
    public MetricsDTO getDashboardMetrics() {
        List<TurnoConteoDTO> empleadosPorTurno = analyticsRepository.countEmployeesBySchedule();
        String turnoConMasEmpleados = empleadosPorTurno.isEmpty() ? "N/A" : empleadosPorTurno.get(0).turno();

        long turnosActivos = empleadosPorTurno.size();
        long turnosInactivos = 0;

        return new MetricsDTO(
                analyticsRepository.countActiveEmployees(),
                turnosActivos,
                turnosInactivos,
                empleadosPorTurno,
                turnoConMasEmpleados,
                analyticsRepository.getEmployeeWithMaxHours(),
                analyticsRepository.getEmployeeWithMinHours(),
                analyticsRepository.getTopPuntuales(),
                analyticsRepository.getTopTardanzas()
        );
    }
}
