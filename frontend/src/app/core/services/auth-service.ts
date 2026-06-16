import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { LoginDto } from '../../shared/models/loginDto';
import { JwtDto } from '../../shared/models/jwtDto';
import { Observable } from 'rxjs';
import { environment } from '../../../environment/environment';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private apiUrl = `${environment.apiUrl}/auth`;

  constructor(private http: HttpClient) { }

  login(loginDto: LoginDto): Observable<JwtDto> {
    return this.http.post<JwtDto>(
      `${this.apiUrl}/login`,
      loginDto
    );
  }
}