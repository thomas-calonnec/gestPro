import {HttpClient} from '@angular/common/http';
import {inject, Injectable} from '@angular/core';
import { Observable } from 'rxjs';
import { Workspace } from '@models/workspace';

import { Card } from '@models/card';
import { User } from '@models/user';
import { environment } from '@environments/environment.development';
import {AuthService} from '@services/auth/auth.service';
import {Router} from '@angular/router';
import {CookieService} from 'ngx-cookie-service';

@Injectable({
  providedIn: 'root'
})
export class UserService {
   router = inject(Router);
  private apiServerUrl = environment.apiUrl + '/user/users';
  authService : AuthService = inject(AuthService);
  private cookieService: CookieService = inject(CookieService)
  constructor(private http: HttpClient) { }


  public getWorkspaces(id: string): Observable<Workspace[]>{

    return this.http.get<Workspace[]>(`${this.apiServerUrl}/${id}/workspaces`,{withCredentials: true});

  }

  public getProtectedData(): Observable<string> {

    return this.http.get<string>(`${this.apiServerUrl}/protected-endpoint`,);
  }


  public addCardToUser(userId: number, card: Card): Observable<Workspace>{
    return this.http.post<Workspace>(`${this.apiServerUrl}/${userId}/addCard`,card);
  }

  public createUser(user : User): Observable<User>{
    return this.http.put<User>(`${this.apiServerUrl}/create`,user);
  }

  public updateUser(userId: number, user: User): Observable<User>{
    return this.http.put<User>(`${this.apiServerUrl}/${userId}`,user);
  }

  public deleteUser(userId: number): Observable<void>{
    return this.http.delete<void>(`${this.apiServerUrl}/${userId}`);
  }

  public createWorkspace(userId: number, workspace: Workspace): Observable<Workspace>{
    return this.http.post<Workspace>(`${this.apiServerUrl}/${userId}/workspace`,workspace);
  }

  public getUserByUsername(username: string): Observable<User>{
/*   const headers = new HttpHeaders({
      'Authorization': `Bearer ${this.authService.getAccessToken()}`, // Ajouter le token JWT dans l'en-tête Authorization
      'Content-Type': 'application/json'
    });*/
    /*var tok =  this.authService.getAccessToken();
    if(tok)
      console.log("username : " + this.authService.isTokenExpired(tok));*/
    return this.http.get<User>(`${this.apiServerUrl}/username/${username}`, {withCredentials: true});
  }


 public getCurrentUser() {
    return this.http.get<any>(`${this.apiServerUrl}/current-user`, {withCredentials: true});
  }
}
