
import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, finalize, tap } from 'rxjs/operators';
import { jwtDecode } from 'jwt-decode';
import { Router } from '@angular/router';
import { environment } from 'src/environments/environments';



@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = `${environment.apiUrl}/api/auth`;
  
  constructor(private http: HttpClient , private router: Router) {}

  /**
   * Authentifie l'utilisateur en envoyant une requête POST à /api/auth/login
   * @param credentials Objet contenant l'email et le mot de passe
   * @returns Observable avec la réponse du backend (JWT, userId, role)
   */
  login(credentials: { email: string; password: string }): Observable<{ accessToken: string; userId: number; role: string }> {
    return this.http.post<{ accessToken: string; userId: number; role: string; tokenType: string; }>(`${this.apiUrl}/login`, credentials).pipe(
      tap(response => {
        console.log('Réponse login reçue:', response);
         const decodedToken: any = jwtDecode(response.accessToken);
         console.log('Decoded Token:', decodedToken);
      const username = decodedToken.username || decodedToken.name || '';
        this.storeAuthData(response. accessToken, response.userId, response.role , username);
      }),
      catchError(this.handleError)
    );
  }

  /**
   * Stocke le JWT, l'ID utilisateur, et le rôle dans localStorage
   * @param jwt Le token JWT
   * @param userId L'ID de l'utilisateur
   * @param role Le rôle de l'utilisateur (ADMIN, SECURITY, MANAGER, EMPLOYEE)
   */
 private storeAuthData(token: string, userId: number, role: string , username?: string): void {
    localStorage.setItem('accessToken', token);
  localStorage.setItem('userId', userId.toString());
  localStorage.setItem('role', role);
   if (username) {
    localStorage.setItem('username', username);
  }
  console.log('✅ Données auth stockées');
 }
clearAuthData(): void {
  console.log('🧹 Suppression des données auth...');
  ['accessToken', 'jwt', 'userId', 'role', 'username'].forEach(key => {
    localStorage.removeItem(key);
    sessionStorage.removeItem(key);
  });
}

  /**
   * Récupère le JWT depuis localStorage
   * @returns Le JWT ou null si non présent
   */
getJwt(): string | null {
  const token = localStorage.getItem('accessToken');
  if (!token) return null;

  try {
    const decoded: any = jwtDecode(token);
    const expiry = decoded.exp * 1000; // Convertir en millisecondes
    if (expiry < Date.now()) {
      console.warn('⚠️ Token expiré, suppression');
      this.clearAuthData();
      return null;
    }
    return token;
  } catch (e) {
    console.error('Erreur de décodage du token:', e);
    this.clearAuthData();
    return null;
  }
}


  /**
   * Récupère l'ID utilisateur depuis localStorage
   * @returns L'ID utilisateur ou null si non présent
   */
  getUserId(): number | null {
    const userId = localStorage.getItem('userId');
    return userId ? parseInt(userId, 10) : null;
  }
  

  /**
   * Récupère le rôle depuis localStorage
   * @returns Le rôle ou null si non présent
   */
  getRole(): string | null {
    return localStorage.getItem('role');
  }
  getusername(): string | null {
    return localStorage.getItem('username');
  }


  /**
   * Vérifie si l'utilisateur est connecté
   * @returns true si un JWT valide est présent, false sinon
   */
  isLoggedIn(): boolean {
    const jwt = this.getJwt();
    return !!jwt; // Simplifié, tu peux ajouter une vérification d'expiration si nécessaire
  }
getUserFromToken(): any {
  const token = this.getJwt();
  if (!token) return null;

  try {
    return jwtDecode(token);
  } catch (e) {
    return null;
  }
}
 getUserRoleFromToken(): any {
    const token = this.getJwt();
    if (!token) return null;
     try {
    return jwtDecode(token);
    } catch (e) {
    return null;
  }}
  /**
   * Déconnecte l'utilisateur en supprimant les données d'authentification
   */
private isLoggingOut = false;

logout(): void {
  if (this.isLoggingOut) return;
  this.isLoggingOut = true;

  const token = this.getJwt();

  const http$ = token
    ? this.http.post<void>('http://localhost:8075/api/auth/logout', {}, {
        headers: { Authorization: `Bearer ${token}` }
      })
    : this.http.post<void>('noop', {}).pipe();     // appel bidon si pas de token

  http$
    .pipe(
      finalize(() => {
        this.isLoggingOut = false;

        /* --- purge toujours --- */
        this.clearAuthData();
        this.router.navigate(['/login']);           // redirection SPA
      })
    )
    .subscribe({
      next: ()   => console.log('✅ Déconnexion côté serveur'),
      error: err => console.warn('⚠️ Déconnexion serveur impossible :', err)
    });
}


  /**
   * Gère les erreurs HTTP
   * @param error L'erreur HTTP
   * @returns Observable avec un message d'erreur
   */
  private handleError(error: HttpErrorResponse): Observable<never> {
    let errorMessage = 'Une erreur est survenue. Veuillez réessayer.';
    if (error.status === 401) {
      errorMessage = 'Email ou mot de passe incorrect.';
    } else if (error.status === 0) {
      errorMessage = 'Impossible de se connecter au serveur. Vérifiez votre connexion.';
    }
    return throwError(() => new Error(errorMessage));
  }
    isAdmin(): boolean {
    // Decode JWT to check role (simplified, use a library like jwt-decode)
    const token = this.getJwt();
    if (token) {
      const payload = JSON.parse(atob(token.split('.')[1]));
      return payload.role === 'Admin';
    }
    return false;
  }
}