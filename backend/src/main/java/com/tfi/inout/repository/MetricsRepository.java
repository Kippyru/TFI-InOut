package com.tfi.inout.repository;

import com.tfi.inout.dto.dashboard.EmpleadoCargaDTO;
import com.tfi.inout.dto.dashboard.EmpleadoPuntualidadDTO;
import com.tfi.inout.dto.dashboard.EmpleadoTardanzaDTO;
import com.tfi.inout.dto.dashboard.TurnoConteoDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MetricsRepository {
    @PersistenceContext
    private EntityManager em;

    public long countActiveEmployees() {
        return em.createQuery("SELECT COUNT(e) FROM Employee e WHERE e.active = true", Long.class)
                .getSingleResult();
    }

    public List<TurnoConteoDTO> countEmployeesBySchedule() {
        String jpql = """
            SELECT new com.tfi.inout.dto.dashboard.TurnoConteoDTO(s.name, COUNT(se.employee.id))
            FROM ScheduleEmployee se
            JOIN se.schedule s
            WHERE se.endDate IS NULL OR se.endDate >= CURRENT_DATE
            GROUP BY s.name
            ORDER BY COUNT(se.employee.id) DESC
            """;
        return em.createQuery(jpql, TurnoConteoDTO.class).getResultList();
    }

    // Usando proyección para obtener la carga horaria máxima
    public EmpleadoCargaDTO getEmployeeWithMaxHours() {
        String jpql = """
            SELECT new com.tfi.inout.dto.dashboard.EmpleadoCargaDTO(CONCAT(e.name, ' ', e.lastName), s.hourWork)
            FROM ScheduleEmployee se
            JOIN se.employee e
            JOIN se.schedule s
            ORDER BY s.hourWork DESC
            """;
        return em.createQuery(jpql, EmpleadoCargaDTO.class).setMaxResults(1).getSingleResult();
    }

    public EmpleadoCargaDTO getEmployeeWithMinHours() {
        String jpql = """
            SELECT new com.tfi.inout.dto.dashboard.EmpleadoCargaDTO(CONCAT(e.name, ' ', e.lastName), s.hourWork)
            FROM ScheduleEmployee se
            JOIN se.employee e
            JOIN se.schedule s
            ORDER BY s.hourWork ASC
            """;
        return em.createQuery(jpql, EmpleadoCargaDTO.class).setMaxResults(1).getSingleResult();
    }

    public List<EmpleadoTardanzaDTO> getTopTardanzas() {
        String jpql = """
        SELECT new com.tfi.inout.dto.dashboard.EmpleadoTardanzaDTO(CONCAT(e.name, ' ', e.lastName), COUNT(ea.id))
        FROM EventAttendance ea
        JOIN ea.employee e
        WHERE ea.eventType = 'CHECK_IN' AND ea.state = 'TARDANZA'
        GROUP BY e.id, e.name, e.lastName
        ORDER BY COUNT(ea.id) DESC
        """;
        // Traemos el Top 5
        return em.createQuery(jpql, EmpleadoTardanzaDTO.class).setMaxResults(5).getResultList();
    }

    public List<EmpleadoPuntualidadDTO> getTopPuntuales() {
        String jpql = """
        SELECT new com.tfi.inout.dto.dashboard.EmpleadoPuntualidadDTO(
            CONCAT(e.name, ' ', e.lastName),
            (SUM(CASE WHEN ea.state = 'PUNTUAL' THEN 1 ELSE 0 END) * 100.0) / COUNT(ea.id)
        )
        FROM EventAttendance ea
        JOIN ea.employee e
        WHERE ea.eventType = 'CHECK_IN' AND ea.state IN ('PUNTUAL', 'TARDANZA')
        GROUP BY e.id, e.name, e.lastName
        HAVING COUNT(ea.id) > 0 
        ORDER BY (SUM(CASE WHEN ea.state = 'PUNTUAL' THEN 1 ELSE 0 END) * 100.0) / COUNT(ea.id) DESC
        """;
        // Traemos el Top 5
        return em.createQuery(jpql, EmpleadoPuntualidadDTO.class).setMaxResults(5).getResultList();
    }

}
