import { Component } from '@angular/core';
import { ViewEncapsulation } from '@angular/core';
import { Router } from '@angular/router';
import { Token } from '../../core/storage/token';

@Component({
  selector: 'app-login',
  imports: [],
  templateUrl: './dashboard.html',
  styleUrl: '../dashboard/dashboard.css',
})
export class Dashboard {

  constructor(
    private token: Token,
    private router: Router
  ) { }

  logout() {
    this.token.removeToken();
    localStorage.clear();
    this.router.navigate(['/login']);
  }
}