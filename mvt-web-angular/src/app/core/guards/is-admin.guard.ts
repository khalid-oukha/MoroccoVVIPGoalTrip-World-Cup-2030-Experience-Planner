import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth.service';
import { map, of, switchMap } from 'rxjs';

export const isAdminGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (!authService.isLoggedIn()) {
    router.navigate(['/auth/sign-in']);
    return false;
  }

  const currentUser = authService.getCurrentUser();

  if (currentUser) {
    const isAdmin = authService.isAdmin();

    if (isAdmin) {
      return true;
    } else {
      router.navigate(['/planner']);
      return false;
    }
  }

  return authService.fetchUserDetails().pipe(
    switchMap(user => {
      if (user && user.authorities && user.authorities.includes('ROLE_ADMIN')) {
        return of(true);
      } else {
        router.navigate(['/planner']);
        return of(false);
      }
    })
  );
};
