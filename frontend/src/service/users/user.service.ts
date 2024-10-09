import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Workspace } from '../../dao/workspace';

import { Card } from '../../dao/card';
import { User } from '../../dao/user';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private apiServerUrl = 'api/users';

  constructor(private http: HttpClient) { }


  public getWorkspaces(userdId: number): Observable<Workspace[]>{
    return this.http.get<Workspace[]>(`${this.apiServerUrl}/${userdId}/workspaces`);
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
  
  public deleteUser(userdId: number): Observable<void>{
    return this.http.delete<void>(`${this.apiServerUrl}/${userdId}`);
  }

  public createWorkspace(userId: number, workspace: Workspace): Observable<Workspace>{
    return this.http.put<Workspace>(`${this.apiServerUrl}/${userId}/workspace`,workspace);
  }
}
