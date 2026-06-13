import {
  Component,
  OnInit,
  OnDestroy,
  HostListener,
  signal,
  computed
} from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { AttendanceService } from '../../services/attendance.service';
import { AttendanceStatusDto, EventAttendanceDto } from '../../models/attendance.model';
import { Employee } from '../../../employee/models/employee.model';

@Component({
  selector: 'app-attendance-clock',
  standalone: true,
  imports: [MatIconModule],
  templateUrl: './attendance-clock.component.html',
  styleUrl: './attendance-clock.component.scss'
})
export class AttendanceClockComponent implements OnInit, OnDestroy {

  // ── Signals ────────────────────────────────────────────────
  loading = signal(true);
  loadError = signal<string | null>(null);
  submitting = signal(false);
  isAdminView = signal(false);

  employee = signal<Employee | null>(null);
  status = signal<AttendanceStatusDto | null>(null);
  successEvent = signal<EventAttendanceDto | null>(null);
  registerError = signal<string | null>(null);
  registerErrorClass = signal<'error' | 'warning' | 'info'>('error');
  registerErrorIcon = signal('error_outline');

  scheduleWarning = signal<string | null>(null);
  canRegister = signal<boolean>(false);
  scheduleWarningClass = signal<'warning'|'error'>('warning');

  // ── Live clock ─────────────────────────────────────────────
  liveClock = signal('');
  liveDate = signal('');

  // ── Device ────────────────────────────────────────────────
  detectedDevice: string = '';

  private clockInterval?: ReturnType<typeof setInterval>;
  private successTimer?: ReturnType<typeof setTimeout>;

  constructor(private attendanceSvc: AttendanceService) { }

  ngOnInit(): void {
    this.detectedDevice = this.attendanceSvc.detectDevice();
    this.startClock();
    this.load();
  }

  ngOnDestroy(): void {
    clearInterval(this.clockInterval);
    clearTimeout(this.successTimer);
  }

  // ── Visibility change: re-query status when user comes back ──
  @HostListener('document:visibilitychange')
  onVisibilityChange(): void {
    if (document.visibilityState === 'visible' && this.employee()) {
      this.refreshStatus();
    }
  }

  // ── Clock ticker ───────────────────────────────────────────
  private startClock(): void {
    const tick = () => {
      const now = new Date();
      this.liveClock.set(
        now.toLocaleTimeString('es-AR', { hour: '2-digit', minute: '2-digit', second: '2-digit', hour12: false })
      );
      this.liveDate.set(
        now.toLocaleDateString('es-AR', { weekday: 'long', day: 'numeric', month: 'long', year: 'numeric' })
      );
    };
    tick();
    this.clockInterval = setInterval(tick, 1000);
  }

  // ── Initial load: resolve employee then status ─────────────
  load(): void {
    this.loading.set(true);
    this.loadError.set(null);
    this.successEvent.set(null);
    this.registerError.set(null);
    this.isAdminView.set(false);

    this.attendanceSvc.getMyEmployee().subscribe({
      next: (emp) => {
        this.employee.set(emp);
        this.refreshStatus();
      },
      error: (err) => {
        if (err?.status === 404) {
          // Admin without an associated employee record
          this.isAdminView.set(true);
          this.loading.set(false);
        } else {
          this.loadError.set('No se pudo obtener los datos del empleado. Verificá tu conexión.');
          this.loading.set(false);
        }
      }
    });
  }

  reload(): void {
    this.load();
  }

  // ── Refresh only the status (called after register or visibility change) ──
  private refreshStatus(): void {
    const emp = this.employee();
    if (!emp?.id) return;

    this.attendanceSvc.getStatus(emp.id as number).subscribe({
      next: (s) => {
        this.status.set(s);
        this.checkScheduleWarning(s);
        this.loading.set(false);
      },
      error: () => {
        this.loadError.set('No se pudo obtener el estado de asistencia. Verificá tu conexión.');
        this.loading.set(false);
      }
    });
  }

  // ── Register attendance ─────────────────────────────────────
  register(): void {
    const emp = this.employee();
    if (!emp?.id || this.submitting()) return;

    this.submitting.set(true);
    this.registerError.set(null);
    this.successEvent.set(null);

    this.attendanceSvc.register(emp.id as number, this.detectedDevice).subscribe({
      next: (event) => {
        this.submitting.set(false);
        this.successEvent.set(event);

        // Auto-reset success panel and re-fetch status after 5 s
        this.successTimer = setTimeout(() => {
          this.successEvent.set(null);
          this.refreshStatus();
        }, 5000);
      },
      error: (err) => {
        this.submitting.set(false);
        this.handleRegisterError(err);
        // Re-fetch status so the button state stays in sync even on error
        this.refreshStatus();
      }
    });
  }

  // ── Evaluate schedule tolerance ──────────────────────────────
  private checkScheduleWarning(s: AttendanceStatusDto): void {
    this.scheduleWarning.set(null);
    this.canRegister.set(true);
    this.scheduleWarningClass.set('warning');

    if (!s.hasSchedule) {
      this.scheduleWarning.set('No tenés un turno asignado para hoy. No es posible marcar asistencia.');
      this.scheduleWarningClass.set('error');
      this.canRegister.set(false);
      return;
    }

    const now = new Date();
    
    const parseTime = (timeStr: string | null | undefined): Date | null => {
      if (!timeStr) return null;
      const [h, m, sec] = timeStr.split(':').map(Number);
      const d = new Date(now);
      d.setHours(h, m, sec || 0, 0);
      return d;
    };

    const toleranceInMs = (s.checkInTolerance ?? 30) * 60 * 1000;
    const toleranceOutMs = (s.checkOutTolerance ?? 30) * 60 * 1000;

    if (s.nextEvent === 'CHECK_IN' && s.scheduledCheckIn) {
      const scheduledIn = parseTime(s.scheduledCheckIn);
      if (scheduledIn && now.getTime() < scheduledIn.getTime() - toleranceInMs) {
        const allowedTime = new Date(scheduledIn.getTime() - toleranceInMs).toLocaleTimeString('es-AR', { hour: '2-digit', minute: '2-digit', hour12: false });
        this.scheduleWarning.set(`Tu turno empieza a las ${this.formatTime(s.scheduledCheckIn)}. Podés marcar entrada a partir de las ${allowedTime}.`);
        this.scheduleWarningClass.set('error');
        this.canRegister.set(false);
      }
    } else if (s.nextEvent === 'CHECK_OUT' && s.scheduledCheckOut) {
      const scheduledOut = parseTime(s.scheduledCheckOut);
      if (scheduledOut && now.getTime() > scheduledOut.getTime() + toleranceOutMs) {
        this.scheduleWarning.set(`Estás marcando salida fuera de la tolerancia de tu horario previsto (${this.formatTime(s.scheduledCheckOut)}).`);
        this.scheduleWarningClass.set('warning');
        // canRegister remains true
      }
    }
  }

  // ── Parse backend error into friendly UX messages ──────────
  private handleRegisterError(err: any): void {
    // Backend returns: { error: { error: "message" } }
    // err.error       → ErrorResponse wrapper object
    // err.error.error → the Map<String,String>, which has key "error"
    const raw = err?.error?.error?.error;
    const backendMsg: string = typeof raw === 'string' ? raw : '';

    if (!err.status || err.status === 0) {
      // Network / timeout
      this.registerError.set(
        'Sin conexión. El evento NO fue registrado. Revisá tu red y reintentá.'
      );
      this.registerErrorClass.set('error');
      this.registerErrorIcon.set('wifi_off');
      return;
    }

    if (backendMsg.toLowerCase().includes('already checked')) {
      // Already registered — show friendly message with last event time if available
      const lastTime = this.status()?.lastEvent
        ? ` (tu último registro fue a las ${this.formatTime(this.status()!.lastEvent!.hour)})`
        : '';
      const action = backendMsg.includes('in') ? 'entrada' : 'salida';
      this.registerError.set(
        `Ya registraste tu ${action} hoy${lastTime}. Si hay un error, contactá a tu supervisor.`
      );
      this.registerErrorClass.set('info');
      this.registerErrorIcon.set('info_outline');
      return;
    }

    if (err.status === 404) {
      this.registerError.set('Empleado no encontrado. Contactá a administración.');
      this.registerErrorClass.set('warning');
      this.registerErrorIcon.set('person_off');
      return;
    }

    // Generic fallback
    this.registerError.set(
      'Ocurrió un error al registrar. Reintentá en unos segundos.'
    );
    this.registerErrorClass.set('error');
    this.registerErrorIcon.set('error_outline');
  }

  // ── Format "HH:mm:ss" → "HH:mm" ───────────────────────────
  formatTime(timeStr: string): string {
    if (!timeStr) return '';
    return timeStr.substring(0, 5); // "08:03"
  }
}

