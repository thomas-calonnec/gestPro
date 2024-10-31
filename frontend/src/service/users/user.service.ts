import {HttpClient, HttpHeaders} from '@angular/common/http';
import {inject, Injectable} from '@angular/core';
import { Observable } from 'rxjs';
import { Workspace } from '../../dao/workspace';

import { Card } from '../../dao/card';
import { User } from '../../dao/user';
import { environment } from '../../environments/environment.development';
import {AuthService} from '../../app/auth.service';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private apiServerUrl = environment.apiUrl + '/users';
  authService : AuthService = inject(AuthService);
  constructor(private http: HttpClient) { }


  public getWorkspaces(id: number): Observable<Workspace[]>{
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${this.authService.getToken()}`, // Ajouter le token JWT dans l'en-tête Authorization
      'Content-Type': 'application/json'
    });

    return this.http.get<Workspace[]>(`${this.apiServerUrl}/${id}/workspaces`, {headers});

  }

  public getProtectedData(): Observable<string> {
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${this.authService.getToken()}`, // Ajouter le token JWT dans l'en-tête Authorization
      'Content-Type': 'application/json'
    });

    return this.http.get<string>(`${this.apiServerUrl}/protected-endpoint`, { headers });
  }


  public addCardToUser(userId: number, card: Card): Observable<User>{
    return this.http.post<User>(`${this.apiServerUrl}/${userId}/addCard`,card);
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
    return this.http.put<Workspace>(`${this.apiServerUrl}/${userId}/workspace`,workspace);
  }

  public getUserByEmail(userEmail: string): Observable<User>{
    return this.http.get<User>(`${this.apiServerUrl}/login/${userEmail}`);
  }
}
