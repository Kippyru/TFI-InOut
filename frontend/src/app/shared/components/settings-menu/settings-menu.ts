import { Component, inject, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ThemeService, AppTheme, AppDensity } from '../../../core/services/theme.service';
import { TranslationService } from '../../../core/services/translation.service';
import { Language } from '../../../core/services/i18n/translations';
import { MaterialModule } from '../../ui/materials-module';

@Component({
  selector: 'app-settings-menu',
  standalone: true,
  imports: [CommonModule, FormsModule, MaterialModule],
  templateUrl: './settings-menu.html',
  styleUrls: ['./settings-menu.scss'],
})
export class SettingsMenu {
  themeService = inject(ThemeService);
  translationService = inject(TranslationService);

  readonly availableThemes = computed<{ value: AppTheme; label: string; color: string }[]>(() => [
    { value: 'azure', label: this.translationService.translate('theme.azure'), color: '#005cbb' },
    { value: 'magenta', label: this.translationService.translate('theme.magenta'), color: '#a60067' },
    { value: 'rose', label: this.translationService.translate('theme.rose'), color: '#9b3f46' },
    { value: 'orange', label: this.translationService.translate('theme.orange'), color: '#8c4e00' },
    { value: 'cyan', label: this.translationService.translate('theme.cyan'), color: '#006874' },
    { value: 'violet', label: this.translationService.translate('theme.violet'), color: '#653198' },
  ]);

  readonly availableDensities = computed<{ value: AppDensity; label: string; description: string }[]>(() => [
    { value: 'normal', label: this.translationService.translate('density.normal'), description: this.translationService.translate('density.normal.desc') },
    { value: 'comfortable', label: this.translationService.translate('density.comfortable'), description: this.translationService.translate('density.comfortable.desc') },
    { value: 'compact', label: this.translationService.translate('density.compact'), description: this.translationService.translate('density.compact.desc') },
  ]);

  readonly availableLanguages: { value: Language; label: string; flag: string }[] = [
    { value: 'es', label: 'Español', flag: '🇪🇸' },
    { value: 'en', label: 'English', flag: '🇺🇸' },
  ];

  t = this.translationService.translate.bind(this.translationService);

  onThemeChange(theme: AppTheme) {
    this.themeService.setTheme(theme);
  }

  onDensityChange(density: AppDensity) {
    this.themeService.setDensity(density);
  }

  onLanguageChange(lang: Language) {
    this.translationService.setLanguage(lang);
  }

  resetToDefaults() {
    this.themeService.setTheme('azure');
    this.themeService.isDarkMode.set(false);
    this.themeService.setDensity('normal');
    this.themeService.animationsEnabled.set(true);
    this.translationService.setLanguage('es');
  }

  isDarkMode = this.themeService.isDarkMode;
  currentTheme = this.themeService.theme;
  currentDensity = this.themeService.density;
  animationsEnabled = this.themeService.animationsEnabled;
  language = this.translationService.language;
}
