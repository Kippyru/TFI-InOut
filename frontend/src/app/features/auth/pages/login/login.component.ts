import { Component, signal, inject } from '@angular/core';
import { AuthService } from '../../../../core/services/auth-service';
import { Token } from '../../../../core/storage/token';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { MaterialModule } from '../../../../shared/ui/materials-module';
import { TranslationService } from '../../../../core/services/translation.service';

@Component({
  selector: 'app-login',
  imports: [FormsModule, MaterialModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class Login {

  username = signal('');
  password = signal('');
  errorMessage = signal('');

  private authService = inject(AuthService);
  private token = inject(Token);
  private router = inject(Router);
  private translationService = inject(TranslationService);

  t = this.translationService.translate.bind(this.translationService);

  login() {
    if (!this.username() || !this.password()) {
      this.errorMessage.set('login.error.empty');
      return; 
    }

    this.errorMessage.set('');

    const loginDto = {
      username: this.username(),
      password: this.password()
    };

    this.authService.login(loginDto).subscribe({
      next: (data) => {
        this.token.saveToken(data.token);
        this.router.navigate(['/dashboard']);
      },
      error: (err) => {
        this.errorMessage.set('login.error.invalid');
      }
    });
  }
}