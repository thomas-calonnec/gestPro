import {inject, Injectable} from '@angular/core';
import { Observable, tap } from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {Router} from '@angular/router';

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
    return this.getToken() !== null;
  }

  logout() : void{
    this.removeToken()

    this.router.navigate(['/login']); // Rediriger vers la page de login après logout

  }





}
