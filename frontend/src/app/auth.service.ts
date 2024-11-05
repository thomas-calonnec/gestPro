import {inject, Injectable} from '@angular/core';
import { Observable, tap } from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {Router} from '@angular/router';
import {jwtDecode} from 'jwt-decode';

@Injectable({
  providedIn: 'root',
})
export class AuthService {

  private apiServerUrl = 'http://192.168.1.138:8080/loginForm';
  private http = inject(HttpClient);
  private router : Router = inject(Router);
  private readonly TOKEN_KEY = "JwtToken";

 private authenticated = false;



  storeToken(token: string)  {
    localStorage.setItem(this.TOKEN_KEY, token) ;
  }

  // Vérifie la présence d'un token à l'initialisation


  getToken(): string | null{
    return localStorage.getItem(this.TOKEN_KEY)
  }

  removeToken() {
    localStorage.removeItem(this.TOKEN_KEY)
  }

  // Méthode pour vérifier si le token a été émis il y a moins d'une heure
  isTokenIssuedWithinLastHour(): boolean {
    const token = this.getToken();
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
  login(username: string, password: string): Observable<any> {
    this.authenticated =  true;
    return this.http.post<any>(this.apiServerUrl , {username, password},{  withCredentials: true }).pipe(
      tap((response) => {
        //this._currentUser.set(response);

        this.storeToken(response.token);

      })
    );

  }

  revokeToken(): Observable<any> {
    return this.http.post<any>(this.apiServerUrl+ '/revoke-token', {}, {withCredentials: true})
    .pipe(
      tap(() => {
        console.log('Tokens refreshed successfully');
      }))
  }

  isAuthenticated(): boolean {

  console.log(this.isTokenIssuedWithinLastHour())
    return this.getToken() !== null && this.isTokenIssuedWithinLastHour();
  }

  logout() : void{
    this.removeToken()

    this.router.navigate(['/login']); // Rediriger vers la page de login après logout

  }





}
