import { inject } from '@angular/core';
import { Router, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { AuthService } from './auth.service';

export const authGuard = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (authService.isLoggedIn()) {
    return true; // Autorise l'accès si l'utilisateur est connecté
  }

  // Redirige vers /login avec l'URL d'origine comme paramètre
  return router.createUrlTree(['/login'], { queryParams: { redirect: state.url } });
};