import {computed, effect, inject, Injectable, signal} from '@angular/core';
import {Observable, tap} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {Router} from '@angular/router';
import {jwtDecode} from 'jwt-decode';
import {environment} from '@environments/environment.development';
import { CookieService } from 'ngx-cookie-service';
import {User} from '@models/user';

@Injectable({
  providedIn: 'root',
})
export class AuthService {

  private apiServerUrl= environment.apiUrl + '/auth'
  private cookieService = inject(CookieService);

  private router : Router = inject(Router);
  private http: HttpClient = inject(HttpClient);
  private _currentUser = signal<User | null>(null);
  currentUser = this._currentUser.asReadonly();
  isConnected = computed(() => this.currentUser() !== null)

  constructor() {
    // Effet qui écoute l'accessToken pour le stocker dans localStorage quand il change
    effect(() => {
    /*  const accessToken = this.accessTokenSignal();
      if (accessToken) {
        localStorage.setItem('accessToken', accessToken);
      } else {
        localStorage.removeItem('accessToken');
      }*/
    });

  }

  getAccessToken(): string | null {
    //return this.accessTokenSignal();
    return this.cookieService.get('accessToken');
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


  setCurrentUser(user: User){
    this._currentUser.set(user);
  }

  public isAuthenticated(): Observable<boolean> {
    return this.http.get<boolean>(`http://localhost:8080/api/user/users/current-user`, { withCredentials: true });
  }
  // Method to check if token is expired based on `exp` field
  isTokenExpired(token: string): boolean {
    const decoded: any = jwtDecode(token);
    if (!decoded.exp) return true; // Treat as expired if no expiry field

    const expiryDate = new Date(decoded.exp * 1000); // Convert `exp` to milliseconds
    return new Date() > expiryDate;
  }

  hasAccessToken(): boolean {
    return this.cookieService.check('accessToken');
  }

  deleteAccessToken(): void {
    this.cookieService.delete('accessToken');
  }
  hasValidAccessToken(): boolean {
    const token = this.getAccessToken();
    return !!token && !this.isTokenExpired(token);
  }

  login(username: string, password: string): Observable<any> {

    return this.http.post<User>(`${this.apiServerUrl}/login`, { username, password }, {withCredentials: true})
  }

  logout() {

    localStorage.removeItem('workspaceId');

    this.cookieService.delete('accessToken');
    this.cookieService.delete('refreshToken');
    this.http.post<any>(`${this.apiServerUrl}/logout`,{},{withCredentials: true}).subscribe({
      next : ()=> {
        this._currentUser.set(null);
        this.router.navigate(['/login']);
      }
    })

  }

  getOAuthGoogle(idToken: string):Observable<any> {

    return this.http.post<any>(`${this.apiServerUrl}/oauth2`, { token: idToken }, // Payload
      { headers: { 'Content-Type': 'application/json' },
        withCredentials: true
      })
  }
  verifyTokenWithBackend(idToken: string) {


    this.getOAuthGoogle(idToken).subscribe({
      next: googleResponse => {
      console.log(googleResponse.user.id)
       // this.setAccessToken(googleResponse.accessToken);
        this._currentUser.set(googleResponse.user)
        localStorage.setItem("USER_ID",googleResponse.user.id.toString())
        this.router.navigateByUrl(`users/${googleResponse.user.id}/workspaces`);

        // window.location.href = `users/${googleResponse.userId}/workspaces`;
      }
    })

  }

  revokeToken() {
    return this.http.post<any>(`${this.apiServerUrl}/revoke-token`, {}, { withCredentials: true })
      .pipe(
        tap(() => {
          // Les nouveaux tokens sont automatiquement stockés dans des cookies HTTP-only
          console.log('Tokens refreshed successfully');
        })
      );
  }
}

