import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from '../../shared/models/user.model';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiUrl = 'http://localhost:8080/api/users';

  constructor(private http: HttpClient) {}

  createUser(user: User): Observable<any> {
    
    return this.http.post(this.apiUrl, user);
  }

  updateUser(id: string | number, user: User): Observable<any> {
    return this.http.put(`${this.apiUrl}/update/${id}`, user, { responseType: 'text' });
  }

  deleteUser(id: string | number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/delete/${id}`, { responseType: 'text' });
  }

  restoreUser(id: string | number): Observable<any> {
    return this.http.put(`${this.apiUrl}/restore/${id}`, {}, { responseType: 'text' });
  }
}
