import { computed, inject, Injectable, signal } from '@angular/core';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import {environment} from '../environments/environment.development';
import { HttpClient } from '@angular/common/http';
import { User } from '../dao/user';

@Injectable({
  providedIn: 'root',
})
export class AuthService {

  private apiServerUrl = environment.apiUrl + '/users';
  private http = inject(HttpClient);
  private _currentUser = signal<User | null>(null);
  currentUser = this._currentUser.asReadonly();
  isConnected  = computed(() => {
    return this._currentUser !== null
  });

  login(email: string, password: string): Observable<{
    user: User
  }> {
    return this.http.post<{
      user: User
    }>(this.apiServerUrl+ '/login', {email, password}).pipe(
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
