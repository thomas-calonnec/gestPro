import {computed, effect, inject, Injectable, Signal, signal} from '@angular/core';
import {catchError, Observable, of, tap} from 'rxjs';
import {HttpClient, HttpHeaders} from '@angular/common/http';
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

  setCurrentUser(user: User){
    this._currentUser.set(user);
  }

  checkAuth() {
    return this.http.get<User>('http://localhost:8080/api/user/users/current-user', { withCredentials: true }).pipe(
      tap(user => this._currentUser.set(user)),
      catchError(err => {
        this._currentUser.set(null);
        return of(null);
      })
    );
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


  login(username: string, password: string): Observable<any> {

    const headers = new HttpHeaders().set('Content-Type', 'application/json');
    return this.http.post<User>(`${this.apiServerUrl}/login`,
      { username, password },
      {
        headers: headers,
        withCredentials: true})
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
      { withCredentials: true}
      )
  }
  verifyTokenWithBackend(idToken: string) {

    this.getOAuthGoogle(idToken).subscribe({
      next: googleResponse => {
      //console.log(googleResponse.user.id)
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
        tap(response => {
          if (response.success) {
            console.log('Token revoked successfully');
          } else {
            console.warn('Token revocation may have failed:', response.message);
          }
        })
      );
  }
}

