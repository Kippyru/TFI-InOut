// token.ts
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class Token {
  private TOKEN_KEY = 'auth-token';

  saveToken(token: string) {
    localStorage.setItem(this.TOKEN_KEY, token);
  }

  getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  removeToken() {
    localStorage.removeItem(this.TOKEN_KEY);
  }

  isLogged(): boolean {
    return this.getToken() != null;
  }

  logout() {
    localStorage.removeItem('auth-token');
  }

  getPayload(): any | null {
    const token = this.getToken();
    if (!token) return null;

    try {
      const payloadBase64 = token.split('.')[1];
      const payloadJson = atob(payloadBase64.replace(/-/g, '+').replace(/_/g, '/'));
      return JSON.parse(payloadJson);
    } catch {
      return null;
    }
  }

  getRole(): number | null {
    const payload = this.getPayload();
    return payload?.role ?? null;
  }

  isAdmin(): boolean {
    return this.getRole() === 1;
  }
}