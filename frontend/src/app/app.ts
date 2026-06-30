import { Component, inject } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { ThemeService } from './core/services/theme.service';
import { TranslationService } from './core/services/translation.service';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet],
  templateUrl: './app.html'
})
export class App {
  constructor() {
    inject(ThemeService);
    inject(TranslationService);
  }
}