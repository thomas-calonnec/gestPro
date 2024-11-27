import {computed, effect, inject, Injectable, signal} from '@angular/core';
import {catchError, map, Observable, of, switchMap, tap} from 'rxjs';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Router} from '@angular/router';
import {jwtDecode} from 'jwt-decode';
import {User} from '../dao/user';
import {environment} from '../environments/environment.development';


@Injectable({
  providedIn: 'root',
})
export class AuthService {

  private http = inject(HttpClient)
  private _currentUser = signal<User | null>(null)
  currentUser = this._currentUser.asReadonly()
  isConnected = computed(() => this.currentUser() !== null)
  private apiServerUrl = environment.apiUrl+'/loginForm';

  login(username: string, password: string): Observable<{
    user: User
  }> {
    return this.http.post<{
      user: User
    }>(this.apiServerUrl+'/login', { username, password }, { withCredentials: true })
      .pipe(
        tap(response => {
          // Les deux tokens sont automatiquement stockés dans des cookies HTTP-only
          // Nous mettons à jour l'état de l'utilisateur connecté
          this._currentUser.set(response.user);
        })
      );
  }

  // Méthode pour rafraîchir les tokens. Utilisée par l'intercepteur HTTP
  revokeToken(): Observable<any> {
    return this.http.post<any>(this.apiServerUrl+'/revoke-token', {}, { withCredentials: true })
      .pipe(
        tap(response => {
          // Les nouveaux tokens sont automatiquement stockés dans des cookies HTTP-only
          console.log('Tokens refreshed successfully');
        })
      );
  }

  logout(): Observable<any> {
    return this.http.post<any>(this.apiServerUrl+'/logout', {}, { withCredentials: true })
      .pipe(
        tap(() => {
          // Le backend devrait supprimer les cookies
          this._currentUser.set(null);
        })
      );
  }
}
