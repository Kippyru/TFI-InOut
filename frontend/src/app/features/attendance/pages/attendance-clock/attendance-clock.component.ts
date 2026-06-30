import {
  Component,
  OnInit,
  OnDestroy,
  HostListener,
  signal
} from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { AttendanceService } from '../../services/attendance.service';
import { AttendanceStatusDto, EventAttendanceDto } from '../../models/attendance.model';
import { Employee } from '../../../employee/models/employee.model';
import { TranslationService } from '../../../../core/services/translation.service';

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

  t!: (key: string) => string;

  constructor(
    private attendanceSvc: AttendanceService,
    private translationService: TranslationService
  ) {
    this.t = this.translationService.translate.bind(this.translationService);
  }

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
          this.loadError.set(this.translationService.translate('clock.errorLoad'));
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
        this.loadError.set(this.translationService.translate('clock.errorStatus'));
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
      // Extraemos solo la hora. Ignoramos los segundos si no son necesarios
      const [h, m] = timeStr.split(':').map(Number);
      const d = new Date(now);
      d.setHours(h, m, 0, 0);
      return d;
    };

    const toleranceInMs = (s.checkInTolerance ?? 0) * 60 * 1000;
    const toleranceOutMs = (s.checkOutTolerance ?? 0) * 60 * 1000;

    if (s.nextEvent === 'CHECK_IN' && s.scheduledCheckIn) {
      const scheduledIn = parseTime(s.scheduledCheckIn);
      if (scheduledIn) {
        const timeDiff = now.getTime() - scheduledIn.getTime();

        // Demasiado temprano
        if (timeDiff < -toleranceInMs) {
          const allowedTime = new Date(scheduledIn.getTime() - toleranceInMs).toLocaleTimeString('es-AR', { hour: '2-digit', minute: '2-digit', hour12: false });
          this.scheduleWarning.set(`Tu turno empieza a las ${this.formatTime(s.scheduledCheckIn)}. Podés marcar entrada a partir de las ${allowedTime}.`);
          this.scheduleWarningClass.set('error');
          this.canRegister.set(false);
        } 
        // Demasiado tarde
        else if (timeDiff > toleranceInMs) {
          const limitTime = new Date(scheduledIn.getTime() + toleranceInMs).toLocaleTimeString('es-AR', { hour: '2-digit', minute: '2-digit', hour12: false });
          this.scheduleWarning.set(`El tiempo límite para registrar entrada expiró a las ${limitTime}.`);
          this.scheduleWarningClass.set('error');
          this.canRegister.set(false); // Bloqueamos el botón también si llega tarde
        }
      }
    } else if (s.nextEvent === 'CHECK_OUT' && s.scheduledCheckOut) {
      const scheduledOut = parseTime(s.scheduledCheckOut);
      if (scheduledOut) {
        // Validación similar para salidas anticipadas
        if (now.getTime() < scheduledOut.getTime() - toleranceOutMs) {
            const allowedTime = new Date(scheduledOut.getTime() - toleranceOutMs).toLocaleTimeString('es-AR', { hour: '2-digit', minute: '2-digit', hour12: false });
            this.scheduleWarning.set(`Aún es temprano para salir. Habilitado a partir de las ${allowedTime}.`);
            this.scheduleWarningClass.set('error');
            this.canRegister.set(false);
        }
      }
    }
  }

  // ── Parse backend error into UX messages ──────────
  private handleRegisterError(err: any): void {
    const raw = err?.error?.error?.error;
    const backendMsg: string = typeof raw === 'string' ? raw : '';

    if (!err.status || err.status === 0) {
      // Network / timeout
      this.registerError.set(
        this.translationService.translate('clock.errorNoConnection')
      );
      this.registerErrorClass.set('error');
      this.registerErrorIcon.set('wifi_off');
      return;
    }

    if (backendMsg.toLowerCase().includes('already checked')) {
      // Already registered
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
      this.registerError.set(this.translationService.translate('clock.errorEmployeeNotFound'));
      this.registerErrorClass.set('warning');
      this.registerErrorIcon.set('person_off');
      return;
    }

    // Generic fallback
    this.registerError.set(
      this.translationService.translate('clock.errorGeneric')
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

