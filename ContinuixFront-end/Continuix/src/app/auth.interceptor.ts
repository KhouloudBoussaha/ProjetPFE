// src/app/auth.interceptor.ts
import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent } from '@angular/common/http';
import { catchError, Observable, throwError } from 'rxjs';
import { AuthService } from './auth.service';


@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  constructor(private authService: AuthService) {}

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const excludedUrls = [
      '/api/users/reset-password',
      '/api/users/forgot-password',
      '/api/auth/login',
      '/api/auth/logout'
     

    ];

    // Vérifie si l'URL actuelle doit être exclue
    const shouldExclude = excludedUrls.some(url => request.url.endsWith(url));


    if (shouldExclude) {
      // Ne pas ajouter de header Authorization
      console.info('🚫 Intercepteur : aucune autorisation ajoutée pour', request.url);
      return next.handle(request);
    }
    const jwt = this.authService.getJwt();
    if (jwt) {
      request = request.clone({
        setHeaders: { Authorization: `Bearer ${jwt}` }
      });
       console.log('✅ JWT ajouté à la requête DELETE :', jwt);
  } else {
    console.warn('⚠️ Aucun token trouvé dans localStorage');
  }
    
   return next.handle(request).pipe(
  catchError(err => {
    if (err.status === 401 || err.status === 403) {
      console.warn('⛔ Token invalide ou expiré, redirection vers /login');
      this.authService.clearAuthData();
      window.location.href = '/login';
    }
    return throwError(() => err);
  })
);
  }
}