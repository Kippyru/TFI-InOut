import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { Token } from '../storage/token';

export const adminGuard: CanActivateFn = () => {
  const tokenService = inject(Token);
  const router = inject(Router);

  if (tokenService.isAdmin()) {
    return true;
  }

  return router.createUrlTree(['/dashboard/attendance']);
};