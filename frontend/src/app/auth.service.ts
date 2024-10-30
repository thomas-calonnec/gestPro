import { computed, inject, Injectable, signal } from '@angular/core';
import { Observable, tap } from 'rxjs';
import {HttpClient} from '@angular/common/http';
import { User } from '../dao/user';

@Injectable({
  providedIn: 'root',
})
export class AuthService {

  private apiServerUrl = 'http://192.168.1.138:8080/loginForm';
  private http = inject(HttpClient);
  private _currentUser = signal<User | null>(null);
  currentUser = this._currentUser.asReadonly();
  tokenKey : string = "jwtToken"
  isConnected  = computed(() => {
    return this._currentUser !== null
  });

  storeToken(token: string)  {
    localStorage.setItem(this.tokenKey, token) ;
  }

  getToken(): string | null{
    return localStorage.getItem(this.tokenKey)
  }
  login(username: string, password: string): Observable<any> {

    return this.http.post<any>(this.apiServerUrl, {username, password},{  withCredentials: true }).pipe(
      tap((response) => {
        //this._currentUser.set(response);
        console.log("Token : " +response.token)
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

  logout() : Observable<any> {
    return this.http.post<any>(this.apiServerUrl+'/logout', {}, {withCredentials: true})
    .pipe(
      tap(() => {
        this._currentUser.set(null);
      })
    )
  }





}
