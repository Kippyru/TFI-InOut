package com.tfi.inout.dto.dashboard;
import java.util.List;

public record MetricsDTO(
        long totalEmpleadosActivos,
        long turnosTrabajandoActualmente,
        long turnosInactivos,
        List<TurnoConteoDTO> empleadosPorTurno,
        String turnoConMasEmpleados,
        EmpleadoCargaDTO empleadoMasCarga,
        EmpleadoCargaDTO empleadoMenosCarga,
        List<EmpleadoPuntualidadDTO> empleadosMasPuntuales,
        List<EmpleadoTardanzaDTO> empleadosMasTardanzas
) {}

