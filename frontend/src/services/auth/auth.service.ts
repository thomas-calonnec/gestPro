import { inject, Injectable} from '@angular/core';
import {catchError, map, Observable, of, tap} from 'rxjs';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Router} from '@angular/router';
import {jwtDecode} from 'jwt-decode';
import {environment} from '@environments/environment.development';
import {User} from '@models/user';
import {authConfig} from '@app/authConfig';
import {OAuthService} from 'angular-oauth2-oidc';

@Injectable({
  providedIn: 'root',
})
export class AuthService {

  private apiServerUrl= environment.apiUrl + '/auth'
  private router : Router = inject(Router);
  private http: HttpClient = inject(HttpClient);
  private currentUser: User | null = null;
  private loggedIn: boolean = false;
  constructor(private oauthService: OAuthService) {
    this.oauthService.configure(authConfig);
  }


  loginGithub() {
    //this.oauthService.initLoginFlow(); // redirige vers GitHub
    const clientId = 'Ov23liGBc9wuOQ9SDN8a';
    const redirectUri = encodeURIComponent('http://localhost:4200/callback');
    const state = crypto.randomUUID(); // facultatif mais recommandé

    const githubAuthUrl =
      `https://github.com/login/oauth/authorize?` +
      `client_id=${clientId}&redirect_uri=${redirectUri}&scope=read:user user:email&state=${state}`;

    // Stocker le state localement pour vérification plus tard
    sessionStorage.setItem('oauth_state', state);

    window.location.href = githubAuthUrl;
  }


  setCurrentUser(user: User) {
    this.currentUser = user;
  }

  getCurrentUser(): User | null {
    return this.currentUser;
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
        withCredentials: true
      })
  }
  logout () {
    this.http.post<void>(`${this.apiServerUrl}/logout`,{},{withCredentials: true}).subscribe({
      next : () => {
          this.router.navigate(['/login']);
      }
    })
  }

  getOAuthGoogle(idToken: string):Observable<any> {

    return this.http.post<any>(`${this.apiServerUrl}/oauth2`, { token: idToken }, {withCredentials: true} // Payload
      )
  }
  verifyTokenWithBackend(idToken: string) {

    this.getOAuthGoogle(idToken).subscribe({
      next: googleResponse => {
       // this.setAccessToken(googleResponse.accessToken);
        this.currentUser = googleResponse.user
        console.log(googleResponse.user)
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
  checkAuth(): Observable<boolean> {
    return this.http.get('http://localhost:8080/api/auth/current-user', { withCredentials: true }).pipe(
      tap(() => this.loggedIn = true),
      map(() => true),
      catchError(() => of(false))
    );
  }
}

