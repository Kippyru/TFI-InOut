import { Injectable, signal, effect, inject } from '@angular/core';
import { DOCUMENT } from '@angular/common';

export type AppTheme = 'azure' | 'magenta' | 'rose' | 'orange' | 'cyan' | 'violet';
export type AppDensity = 'normal' | 'comfortable' | 'compact';

@Injectable({
  providedIn: 'root'
})
export class ThemeService {
  private document = inject(DOCUMENT);

  isDarkMode = signal<boolean>(false);
  theme = signal<AppTheme>('azure');
  density = signal<AppDensity>('normal');
  animationsEnabled = signal<boolean>(true);

  constructor() {
    this.loadPreferences();

    effect(() => {
      this.applySettingsToDOM(
        this.isDarkMode(),
        this.theme(),
        this.density(),
        this.animationsEnabled()
      );
      this.savePreferences();
    });
  }

  toggleDarkMode(): void { this.isDarkMode.update(v => !v); }
  setTheme(t: AppTheme): void { this.theme.set(t); }
  setDensity(d: AppDensity): void { this.density.set(d); }
  toggleAnimations(): void { this.animationsEnabled.update(v => !v); }

  private loadPreferences() {
    const dark = localStorage.getItem('app-dark-mode');
    if (dark !== null) this.isDarkMode.set(dark === 'true');

    const theme = localStorage.getItem('app-theme') as AppTheme;
    if (theme) this.theme.set(theme);

    const density = localStorage.getItem('app-density') as AppDensity;
    if (density) this.density.set(density);

    const anim = localStorage.getItem('app-animations');
    if (anim !== null) this.animationsEnabled.set(anim === 'true');
  }

  private savePreferences() {
    localStorage.setItem('app-dark-mode', String(this.isDarkMode()));
    localStorage.setItem('app-theme', this.theme());
    localStorage.setItem('app-density', this.density());
    localStorage.setItem('app-animations', String(this.animationsEnabled()));
  }

  private applySettingsToDOM(dark: boolean, theme: AppTheme, density: AppDensity, animations: boolean) {
    const html = this.document.documentElement;

    html.classList.remove('dark-theme');
    html.classList.remove('theme-azure', 'theme-magenta', 'theme-rose', 'theme-orange', 'theme-cyan', 'theme-violet');
    html.classList.remove('density-normal', 'density-comfortable', 'density-compact');
    html.classList.remove('no-animations');

    if (dark) html.classList.add('dark-theme');
    if (theme !== 'azure') html.classList.add(`theme-${theme}`);
    if (density !== 'normal') html.classList.add(`density-${density}`);
    if (!animations) html.classList.add('no-animations');
  }
}