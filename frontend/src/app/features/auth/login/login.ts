import { Component } from '@angular/core';
import { AuthService } from '../../../core/services/auth-service';
import { Token } from '../../../core/storage/token';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  imports: [FormsModule],
  templateUrl: './login.html',
  styleUrls: ['../login/login.css'],
})
export class Login {

  username: string = '';
  password: string = '';
  errorMessage: string = '';

  constructor(
    private authService: AuthService,
    private Token: Token,
    private router: Router
  ) { }

  login() {

    const loginDto = {
      username: this.username,
      password: this.password
    };

    this.authService.login(loginDto).subscribe({
      next: (data) => {

        this.Token.saveToken(data.token);

        console.log("Token guardado:", data.token);

        this.router.navigate(['/dashboard']);
      },
      error: () => {
        this.errorMessage = 'Usuario o contraseña incorrectos';
      }
    });
  }
}