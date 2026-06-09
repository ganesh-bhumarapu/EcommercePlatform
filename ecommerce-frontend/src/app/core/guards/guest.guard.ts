import { inject } from '@angular/core';
import { Router, type CanActivateFn } from '@angular/router';
import { AuthService } from '../services/auth.service';

export const guestGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (authService.isLoggedIn()) {
    // Already logged in, redirect to home or admin dashboard
    if (authService.isAdmin()) {
      router.navigate(['/admin']);
    } else {
      router.navigate(['/']);
    }
    return false;
  }

  // Not logged in, allow access
  return true;
};
