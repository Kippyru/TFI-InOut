import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AttendanceStatusDto, EventAttendanceDto, DailyAttendanceDto } from '../models/attendance.model';
import { Employee } from '../../employee/models/employee.model';
import { environment } from '../../../../environment/environment';

@Injectable({
  providedIn: 'root'
})
export class AttendanceService {
  private attendanceUrl = `${environment.apiUrl}/attendance`;
  private employeeUrl = `${environment.apiUrl}/employees`;
  private adminAttendanceUrl = `${environment.apiUrl}/admin/attendance`;

  constructor(private http: HttpClient) { }

  /** Resolves the current logged-in user's employee record */
  getMyEmployee(): Observable<Employee> {
    return this.http.get<Employee>(`${this.employeeUrl}/me`);
  }

  /** Returns next expected event, last event today, and schedule status */
  getStatus(employeeId: number): Observable<AttendanceStatusDto> {
    return this.http.get<AttendanceStatusDto>(`${this.attendanceUrl}/${employeeId}/status`);
  }

  /** Registers the attendance event (POST) */
  register(employeeId: number, device: string): Observable<EventAttendanceDto> {
    return this.http.post<EventAttendanceDto>(
      `${this.attendanceUrl}/${employeeId}?device=${encodeURIComponent(device)}`,
      {}
    );
  }

  /** Gets the daily attendance for all active employees */
  getDailyAttendance(date: string): Observable<DailyAttendanceDto[]> {
    return this.http.get<DailyAttendanceDto[]>(`${this.adminAttendanceUrl}/daily`, {
      params: { date }
    });
  }

  getAttendance(date: string): Observable<EventAttendanceDto[]> {
    return this.http.get<EventAttendanceDto[]>(`${this.attendanceUrl}/list`, {
      params: { date }
    });
  }

  /** Audits (edits) an attendance event */
  auditAttendance(adminId: number, eventAttendanceId: number, reason: string, newValue: string): Observable<any> {
    return this.http.post<any>(`${this.adminAttendanceUrl}/audit`, null, {
      params: {
        adminId: adminId.toString(),
        eventAttendanceId: eventAttendanceId.toString(),
        reason,
        newValue
      }
    });
  }

  /** Detects the device type from the User-Agent for the `device` param */
  detectDevice(): string {
    const ua = navigator.userAgent.toLowerCase();
    const platform = navigator.platform?.toLowerCase() ?? '';

    // OS detection
    let os = 'unknown';
    if (/android/.test(ua)) os = 'android';
    else if (/iphone|ipod/.test(ua)) os = 'ios-iphone';
    else if (/ipad/.test(ua)) os = 'ios-ipad';
    else if (/windows/.test(ua)) os = 'windows';
    else if (/macintosh|mac os/.test(ua)) os = 'mac';
    else if (/linux/.test(ua)) os = 'linux';

    // Browser detection
    let browser = 'browser';
    if (/edg\//.test(ua)) browser = 'edge';
    else if (/opr\/|opera/.test(ua)) browser = 'opera';
    else if (/chrome/.test(ua) && !/chromium/.test(ua)) browser = 'chrome';
    else if (/firefox/.test(ua)) browser = 'firefox';
    else if (/safari/.test(ua) && !/chrome/.test(ua)) browser = 'safari';

    return `${os}-${browser}`;  // e.g. "android-chrome", "windows-edge"
  }
}
