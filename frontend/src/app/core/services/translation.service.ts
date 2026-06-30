import { Injectable, signal, computed, effect } from '@angular/core';
import { TRANSLATIONS, Language } from './i18n/translations';

@Injectable({
  providedIn: 'root',
})
export class TranslationService {
  language = signal<Language>('es');
  private data = computed(() => TRANSLATIONS[this.language()]);

  constructor() {
    const saved = localStorage.getItem('app-language') as Language;
    if (saved === 'es' || saved === 'en') {
      this.language.set(saved);
    }

    effect(() => {
      localStorage.setItem('app-language', this.language());
    });
  }

  translate(key: string): string {
    return this.data()[key] ?? key;
  }

  setLanguage(lang: Language) {
    this.language.set(lang);
  }
}
