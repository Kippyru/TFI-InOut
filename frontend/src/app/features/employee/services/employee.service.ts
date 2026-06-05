import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Employee } from '../models/employee.model';

@Injectable({
  providedIn: 'root'
})
export class EmployeeService {
  private apiUrl = 'http://localhost:8080/api/employees';

  constructor(private http: HttpClient) {}

  getEmployees(): Observable<Employee[]> {
    return this.http.get<Employee[]>(`${this.apiUrl}/list`);
  }

  getEmployeeById(id: string | number): Observable<Employee> {
    return this.http.get<Employee>(`${this.apiUrl}/list/${id}`);
  }

  createEmployee(employee: Employee): Observable<any> {
    return this.http.post(this.apiUrl, employee, { responseType: 'text' });
  }

  updateEmployee(id: string | number, employee: Employee): Observable<any> {
    return this.http.put(`${this.apiUrl}/update/${id}`, employee, { responseType: 'text' });
  }

  deleteEmployee(id: string | number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/delete/${id}`, { responseType: 'text' });
  }

  restoreEmployee(id: string | number): Observable<any> {
    return this.http.put(`${this.apiUrl}/restore/${id}`, {}, { responseType: 'text' });
  }
}
