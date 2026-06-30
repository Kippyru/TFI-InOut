import { ES } from './es';
import { EN } from './en';

export type Language = 'es' | 'en';

export const TRANSLATIONS: Record<Language, Record<string, string>> = {
  es: ES,
  en: EN,
};
