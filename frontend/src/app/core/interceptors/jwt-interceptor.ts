import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { Token } from '../storage/token';

export const jwtInterceptor: HttpInterceptorFn = (req, next) => {

  const tokenService = inject(Token);

  const token = tokenService.getToken();

  if (token) {

    const cloned = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });

    return next(cloned);
  }

  return next(req);
};