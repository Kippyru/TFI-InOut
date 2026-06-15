import { Component, signal } from '@angular/core';
import { AuthService } from '../../../core/services/auth-service';
import { Token } from '../../../core/storage/token';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { MaterialModule } from '../../../shared/ui/materials-module';

@Component({
  selector: 'app-login',
  imports: [FormsModule, MaterialModule],
  templateUrl: './login.html',
  styleUrls: ['../login/login.scss'],
})
export class Login {

  username: string = '';
  password: string = '';
  errorMessage = signal('');

  constructor(
    private authService: AuthService,
    private Token: Token,
    private router: Router
  ) { }

  login() {
    if (!this.username || !this.password) {
      this.errorMessage.set('Por favor, complete todos los campos');
      return; 
    }

    this.errorMessage.set('');

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
      error: (err) => {
        this.errorMessage.set('Usuario o contraseña incorrectos');
        console.error('Error de login:', err);
      }
    });
  }
}