import {effect, inject, Injectable, signal} from '@angular/core';
import {catchError, Observable, tap, throwError} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {Router} from '@angular/router';
import {jwtDecode} from 'jwt-decode';


@Injectable({
  providedIn: 'root',
})
export class AuthService {

  private apiServerUrl = "http://localhost:8080/loginForm";
  private accessTokenSignal = signal<string | null>(localStorage.getItem('accessToken'));
  private refreshTokenSignal = signal<string | null>(localStorage.getItem('refreshToken'));

  private router : Router = inject(Router);
  private http: HttpClient = inject(HttpClient);

  constructor() {
    // Effet qui écoute l'accessToken pour le stocker dans localStorage quand il change
    effect(() => {
      const accessToken = this.accessTokenSignal();
      if (accessToken) {
        localStorage.setItem('accessToken', accessToken);
      } else {
        localStorage.removeItem('accessToken');
      }
    });

    // Effet qui écoute le refreshToken pour le stocker dans localStorage quand il change
    effect(() => {
      const refreshToken = this.refreshTokenSignal();
      if (refreshToken) {
        localStorage.setItem('refreshToken', refreshToken);
      } else {
        localStorage.removeItem('refreshToken');
      }
    });
  }

  getAccessToken(): string | null {
    return this.accessTokenSignal();
  }
  // Méthode pour vérifier si le token a été émis il y a moins d'une heure
  isTokenIssuedWithinLastHour(): boolean {
    const token = this.getAccessToken();
    if (!token) return false;

    // Décoder le token pour obtenir la date d'émission (iat)
    const decoded: any = jwtDecode(token);
    if (!decoded.iat) return false; // Assurez-vous que le champ `iat` est présent

    const issuedAt = new Date(decoded.iat * 1000); // Convertir `iat` en millisecondes
    const now = new Date();
    console.log("issued at : " );
    // Calculer la différence en millisecondes et la convertir en heures
    const diffInHours = (now.getTime() - issuedAt.getTime()) / (1000 * 60 * 60);

    return diffInHours < 1; // Retourne true si le token a moins d'une heure
  }


  isAuthenticated(): boolean {

    return this.getAccessToken() !== null && this.isTokenIssuedWithinLastHour();
  }
  setAccessToken(token: string): void {
    this.accessTokenSignal.set(token);
  }

  setRefreshToken(token: string): void {
    this.refreshTokenSignal.set(token);
  }

  removeTokens(): void {
    this.accessTokenSignal.set(null);
    this.refreshTokenSignal.set(null);
  }

  // Méthode pour renouveler le token d'accès en utilisant le token de rafraîchissement
  refreshToken(): Observable<any> {
    return this.http.post(this.apiServerUrl+'/refresh-token', { refreshToken: this.refreshTokenSignal() }).pipe(
      tap((response: any) => {
        this.setAccessToken(response.accessToken);
      }),
      catchError(() => {
        this.removeTokens();
        this.router.navigate(['/login']);
        return throwError(() => new Error('Token de rafraîchissement invalide ou expiré'));
      })
    );
  }


  login(username: string, password: string): Observable<any> {

    return this.http.post<any>(this.apiServerUrl,{username, password}, {withCredentials: true}).pipe(
      tap((response: any) => {

        this.setRefreshToken(response.refreshToken);

      })
    );

  }

  logout() {
    this.removeTokens();
    localStorage.removeItem('workspaceId');
    this.router.navigate(['/login']);
  }
}
