import { CanActivateFn } from '@angular/router';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { Token } from '../storage/token';

export const authGuard: CanActivateFn = () => {

  const tokenService = inject(Token);
  const router = inject(Router);

  if (tokenService.isLogged()) {
    return true;
  }

  router.navigate(['/login']);
  return false;
};