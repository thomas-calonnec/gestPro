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
  isConnected  = computed(() => {
    return this._currentUser !== null
  });


  login(username: string, password: string): Observable<{
    user: User
  }> {

    return this.http.post<{
      user: User
    }>(this.apiServerUrl, {username, password},{  withCredentials: true }).pipe(
      tap((response) => {
        this._currentUser.set(response.user);
      })
    );

  }

  revokeToken(): Observable<any> {
    return this.http.post<any>(this.apiServerUrl+ '/revoke-token', {}, {withCredentials: true})
    .pipe(
      tap(response => {
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
