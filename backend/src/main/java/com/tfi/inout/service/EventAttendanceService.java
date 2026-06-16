package com.tfi.inout.service;

import com.tfi.inout.dto.AttendanceStatusDto;
import com.tfi.inout.dto.EventAttendanceDto;
import com.tfi.inout.mapper.EventAttendanceMapper;
import com.tfi.inout.model.DetailSchedule;
import com.tfi.inout.model.Employee;
import com.tfi.inout.model.EventAttendance;
import com.tfi.inout.model.ScheduleEmployee;
import com.tfi.inout.repository.DetailScheduleRepository;
import com.tfi.inout.repository.EmployeeRepository;
import com.tfi.inout.repository.EventAttendanceRepository;
import com.tfi.inout.repository.ScheduleEmployeeRepository;
import com.tfi.inout.handler.ResourceNotFoundException;
import com.tfi.inout.handler.BusinessException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventAttendanceService {

    private final EventAttendanceRepository repository;
    private final EmployeeRepository employeeRepository;
    private final EventAttendanceMapper mapper;
    private final ScheduleEmployeeRepository scheduleEmployeeRepository;
    private final DetailScheduleRepository detailScheduleRepository;
    private final EventAttendanceMapper eventAttendanceMapper;

    public AttendanceStatusDto getAttendanceStatus(Long employeeId) {
        employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        LocalDate today = LocalDate.now();

        Optional<ScheduleEmployee> scheduleEmployeeOpt = scheduleEmployeeRepository
                .findActiveScheduleByEmployeeAndDate(employeeId, today);
        boolean hasSchedule = scheduleEmployeeOpt.isPresent();

        LocalTime scheduledCheckIn = null;
        LocalTime scheduledCheckOut = null;
        Integer checkInTolerance = null;
        Integer checkOutTolerance = null;

        if (hasSchedule) {
            String dayOfWeek = getLocalizedDayOfWeek(today);

            com.tfi.inout.model.Schedule schedule = scheduleEmployeeOpt.get().getSchedule();
            checkInTolerance = schedule.getCheckInTolerance();
            checkOutTolerance = schedule.getCheckOutTolerance();

            Optional<DetailSchedule> detailOpt = detailScheduleRepository
                    .findByScheduleIdAndDayIgnoreCase(schedule.getId(), dayOfWeek);

            if (detailOpt.isPresent()) {
                DetailSchedule detail = detailOpt.get();
                scheduledCheckIn = detail.getCheckIn();
                scheduledCheckOut = detail.getCheckOut();
            } else {
                //Si tiene turno asignado pero no trabaja este día en específico
                hasSchedule = false;
            }
        }

        String nextEvent = determineEventType(employeeId, today);

        EventAttendanceDto lastEvent = repository.findByEmployeeIdAndDate(employeeId, today)
                .stream()
                .max((e1, e2) -> e1.getHour().compareTo(e2.getHour()))
                .map(mapper::toDto)
                .orElse(null);

        return new AttendanceStatusDto(nextEvent, lastEvent, hasSchedule, scheduledCheckIn, scheduledCheckOut, checkInTolerance, checkOutTolerance);
    }

    @Transactional
    public EventAttendanceDto registerAttendance(Long employeeId, String device) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        AttendanceStatusDto status = getAttendanceStatus(employeeId);

        if (!status.isHasSchedule()) {
            throw new BusinessException("No tenes un turno asignado para el día de hoy o es tu día libre.");
        }

        String nextEvent = status.getNextEvent();

        Optional<EventAttendance> existingEvent = repository.findByEmployeeIdAndDateAndEventType(employeeId, today, nextEvent);
        if (existingEvent.isPresent()) {
            throw new BusinessException("Ya registraste tu " + (nextEvent.equals("CHECK_IN") ? "entrada" : "salida") + " de hoy.");
        }

        validateTimeTolerance(status, nextEvent, now);

        EventAttendance attendance = new EventAttendance();
        attendance.setEmployee(employee);
        attendance.setEventType(nextEvent);
        attendance.setHour(now);
        attendance.setDate(today);
        attendance.setDevice(device);
        attendance.setState("ACTIVE"); // esto deberia sacarlo porq no se usa mas, ahora se usa active, pero no molesta

        attendance = repository.save(attendance);

        return mapper.toDto(attendance);
    }

    private void validateTimeTolerance(AttendanceStatusDto status, String nextEvent, LocalTime now) {
        if ("CHECK_IN".equals(nextEvent) && status.getScheduledCheckIn() != null) {
            LocalTime scheduledIn = status.getScheduledCheckIn();
            int tolerance = status.getCheckInTolerance() != null ? status.getCheckInTolerance() : 0;

            LocalTime startWindow = scheduledIn.minusMinutes(tolerance);
            LocalTime endWindow = scheduledIn.plusMinutes(tolerance);

            if (now.isBefore(startWindow)) {
                throw new BusinessException("Aún es temprano. Puedes registrar tu entrada a partir de las " + startWindow);
            }
            if (now.isAfter(endWindow)) {
                throw new BusinessException("Tu tiempo de tolerancia para el ingreso ha expirado. (Límite: " + endWindow + ")");
            }
        }
        else if ("CHECK_OUT".equals(nextEvent) && status.getScheduledCheckOut() != null) {
            LocalTime scheduledOut = status.getScheduledCheckOut();
            int tolerance = status.getCheckOutTolerance() != null ? status.getCheckOutTolerance() : 0;

            LocalTime startWindow = scheduledOut.minusMinutes(tolerance);

            if (now.isBefore(startWindow)) {
                throw new BusinessException("Aún no es horario de salida. Habilitado a partir de las " + startWindow);
            }
        }
    }

    //determina el tipo de vento en base del historial del día en BD
    public String determineEventType(Long employeeId, LocalDate date) {
        Optional<EventAttendance> lastEventOpt = repository.findByEmployeeIdAndDate(employeeId, date).stream()
                .max((e1, e2) -> e1.getHour().compareTo(e2.getHour()));

        if (lastEventOpt.isEmpty()) {
            return "CHECK_IN";
        }
        return lastEventOpt.get().getEventType().equals("CHECK_IN") ? "CHECK_OUT" : "CHECK_IN";
    }

    public List<EventAttendanceDto> list(LocalDate date) {
        List<EventAttendance> attendances = (date != null)
                ? repository.findByDate(date)
                : repository.findAll();
        return eventAttendanceMapper.toList(attendances);
    }

    //Adapta el nombre del día de Java a español porq en el front esta en español...... habria que refactorizar el front
    private String getLocalizedDayOfWeek(LocalDate date) {
        return switch (date.getDayOfWeek()) {
            case MONDAY -> "Lunes";
            case TUESDAY -> "Martes";
            case WEDNESDAY -> "Miércoles";
            case THURSDAY -> "Jueves";
            case FRIDAY -> "Viernes";
            case SATURDAY -> "Sábado";
            case SUNDAY -> "Domingo";
        };
    }
}