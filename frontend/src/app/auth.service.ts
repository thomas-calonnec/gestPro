import {effect, inject, Injectable, NgZone, signal} from '@angular/core';
import {catchError, Observable, tap, throwError} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {Router} from '@angular/router';
import {environment} from '@environments/environment.development';
import {Token} from '@models/token';
import {jwtDecode} from 'jwt-decode';

declare const google: any;

@Injectable({
  providedIn: 'root',
})
export class AuthService {

  private apiServerUrl= environment.apiUrl + '/auth'
  private accessTokenSignal = signal<string | null>(localStorage.getItem('accessToken'));

  private router : Router = inject(Router);
  private http: HttpClient = inject(HttpClient);

  constructor(private ngZone: NgZone) {
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
  isTokenExpired(token: Token): boolean {
    //const decoded: any = jwtDecode(token);
    if (!token.exp) return true; // Treat as expired if no expiry field

    const expiryDate = new Date(token.exp * 1000); // Convert `exp` to milliseconds
    return new Date() > expiryDate;
  }

  // hasValidAccessToken(): boolean {
  //   const token = this.getAccessToken();
  //   return !!token && !this.isTokenExpired(token);
  // }

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

  getOAuthGoogle(idToken: string):Observable<any> {

    return this.http.post<any>(`${this.apiServerUrl}/oauth2`, { token: idToken }, // Payload
      { headers: { 'Content-Type': 'application/json' }
      })
  }


  initializeGoogleAuth(): void {
    // Vérifiez si la bibliothèque Google est chargée
    if (typeof google === 'undefined') {
      console.error('Google Identity Services n’est pas chargé.');
      return;
    }

    google.accounts.id.initialize({
      client_id: '392803604648-s1fsi45jpjictprj577ev1akjtcivopr.apps.googleusercontent.com',
      callback: (response: any) => this.handleCredentialResponse(response),
      use_fedcm_for_prompt: true, // Activer FedCM
    });

    // Rendre le bouton Google Sign-In


    // Optionnel : affichage automatique du prompt
    google.accounts.id.prompt();
  }

  // Fonction callback pour gérer la réponse après authentification
  handleCredentialResponse(response: any): void {
    this.ngZone.run(() => {
      console.log('Token JWT reçu via FedCM :', response.credential);

      // Envoyer le token au backend si nécessaire
      this.verifyTokenWithBackend(response.credential);
    });
  }

  verifyTokenWithBackend(idToken: string) {


    this.getOAuthGoogle(idToken).subscribe({
      next: googleResponse => {

        this.setAccessToken(googleResponse.expiration);
        this.router.navigate([`users/${googleResponse.googleId}/workspaces`]);

        // window.location.href = `users/${googleResponse.googleId}/workspaces`;
      }
    })

  }
}
