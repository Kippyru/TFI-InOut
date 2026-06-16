export interface TurnoConteo {
  turno: string;
  cantidad: number;
}

export interface EmpleadoCarga {
  nombre: string;
  horas: number;
}

export interface EmpleadoPuntualidad {
  nombre: string;
  porcentaje: number;
}

export interface EmpleadoTardanza {
  nombre: string;
  cantidad: number;
}

export interface DashboardMetrics {
  totalEmpleadosActivos: number;
  turnosTrabajandoActualmente: number;
  turnosInactivos: number;
  empleadosPorTurno: TurnoConteo[];
  turnoConMasEmpleados: string;
  empleadoMasCarga: EmpleadoCarga;
  empleadoMenosCarga: EmpleadoCarga;
  empleadosMasPuntuales: EmpleadoPuntualidad[];
  empleadosMasTardanzas: EmpleadoTardanza[];
}