import {effect, inject, Injectable, signal} from '@angular/core';
import {catchError, Observable, tap, throwError} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {Router} from '@angular/router';
import {jwtDecode} from 'jwt-decode';
import {environment} from '@environments/environment.development';


@Injectable({
  providedIn: 'root',
})
export class AuthService {

  private apiServerUrl= environment.apiUrl + '/auth'
  private accessTokenSignal = signal<string | null>(localStorage.getItem('accessToken'));

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


  removeTokens(): void {
    this.accessTokenSignal.set(null);

  }

  // Method to check if token is expired based on `exp` field
  isTokenExpired(token: string): boolean {
    const decoded: any = jwtDecode(token);
    if (!decoded.exp) return true; // Treat as expired if no expiry field

    const expiryDate = new Date(decoded.exp * 1000); // Convert `exp` to milliseconds
    return new Date() > expiryDate;
  }

  hasValidAccessToken(): boolean {
    const token = this.getAccessToken();
    return !!token && !this.isTokenExpired(token);
  }

  refreshAccessToken(): Observable<any> {

    return this.http.post<any>(`${this.apiServerUrl}/refresh`, {})
      .pipe(
        tap((response: any) => {
          this.setAccessToken(response.accessToken);

        }),
        catchError(error => {
          console.error('Token refresh failed:', error);
          return throwError(() => error);
        })
      );
  }


  login(username: string, password: string): Observable<any> {


    return this.http.post<any>(`${this.apiServerUrl}/login`, { username, password }, {withCredentials: true})
      .pipe(
        tap((response) => {
            this.setAccessToken(response.accessToken)
        })


      );


  }

  logout() {
    this.removeTokens();
    localStorage.removeItem('workspaceId');
    this.router.navigate(['/login']);
  }
}
