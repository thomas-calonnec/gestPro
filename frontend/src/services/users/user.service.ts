import {HttpClient} from '@angular/common/http';
import {inject, Injectable} from '@angular/core';
import { Observable } from 'rxjs';
import { Workspace } from '@models/workspace';

import { Card } from '@models/card';
import { User } from '@models/user';
import { environment } from '@environments/environment.development';
import {Router} from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class UserService {
   router = inject(Router);
  private apiServerUrl = environment.apiUrl + '/user/users';
  constructor(private http: HttpClient) { }

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

  public getUserByUsername(username: string): Observable<User>{
    return this.http.get<User>(`${this.apiServerUrl}/username/${username}`, {withCredentials: true});
  }

  public getUserById(userId: number) {
    return this.http.get<User>(`${this.apiServerUrl}/id/${userId}`, {withCredentials : true});
  }

  public getAllUser() {
    return this.http.get<User[]>(`${this.apiServerUrl}`,{withCredentials: true});
  }

}
