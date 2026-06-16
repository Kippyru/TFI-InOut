import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ScheduleDto, DetailScheduleDto, ScheduleEmployeeDto } from '../models/schedule.model';
import { environment } from '../../../../environment/environment';

@Injectable({
  providedIn: 'root'
})
export class ScheduleService {
  private apiUrl = `${environment.apiUrl}/schedules`; 

  constructor(private http: HttpClient) { }

  listSchedules(): Observable<ScheduleDto[]> {
    return this.http.get<ScheduleDto[]>(this.apiUrl);
  }

  getScheduleById(id: string | number): Observable<ScheduleDto> {
    return this.http.get<ScheduleDto>(`${this.apiUrl}/list/${id}`);
  }

  createSchedule(schedule: ScheduleDto): Observable<ScheduleDto> {
    return this.http.post<ScheduleDto>(this.apiUrl, schedule);
  }

  updateSchedule(id: string | number, schedule: ScheduleDto): Observable<ScheduleDto> {
    return this.http.put<ScheduleDto>(`${this.apiUrl}/update/${id}`, schedule);
  }

  deleteSchedule(id: string | number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/delete/${id}`, { responseType: 'text' });
  }

  restoreSchedule(id: string | number): Observable<any> {
    return this.http.put(`${this.apiUrl}/restore/${id}`, {}, { responseType: 'text' });
  }

  addDetailToSchedule(id: string | number, detail: DetailScheduleDto): Observable<DetailScheduleDto> {
    return this.http.post<DetailScheduleDto>(`${this.apiUrl}/${id}/details`, detail);
  }

  addDetailsBulkToSchedule(id: string | number, details: DetailScheduleDto[]): Observable<DetailScheduleDto[]> {
    return this.http.post<DetailScheduleDto[]>(`${this.apiUrl}/${id}/details/bulk`, details);
  }

  getDetailsBySchedule(id: string | number): Observable<DetailScheduleDto[]> {
    return this.http.get<DetailScheduleDto[]>(`${this.apiUrl}/${id}/details`);
  }

  updateDetail(detailId: string | number, detail: DetailScheduleDto): Observable<DetailScheduleDto> {
    return this.http.put<DetailScheduleDto>(`http://localhost:8080/api/detail-schedules/${detailId}`, detail);
  }

  deleteDetail(detailId: string | number): Observable<any> {
    return this.http.delete(`http://localhost:8080/api/detail-schedules/${detailId}`, { responseType: 'text' });
  }

  getActiveScheduleForEmployee(employeeId: string | number): Observable<ScheduleEmployeeDto> {
    return this.http.get<ScheduleEmployeeDto>(`${this.apiUrl}/employee/${employeeId}/active`);
  }

  getAllSchedulesForEmployee(employeeId: string | number): Observable<ScheduleEmployeeDto[]> {
    return this.http.get<ScheduleEmployeeDto[]>(`${this.apiUrl}/employee/${employeeId}`);
  }

  assignSchedule(assignment: ScheduleEmployeeDto): Observable<ScheduleEmployeeDto> {
    return this.http.post<ScheduleEmployeeDto>(`${this.apiUrl}/assign`, assignment);
  }
}
