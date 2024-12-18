import {inject, Injectable} from '@angular/core';
import { Observable } from 'rxjs';
import { Board } from '../../dao/board';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {environment} from '../../environments/environment.development';
import {Workspace} from '../../dao/workspace';
import {AuthService} from '../../app/auth.service';


@Injectable({
  providedIn: 'root'
})
export class WorkspaceService {

  private apiServerUrl = environment.apiUrl + '/user/workspaces';
  private authService : AuthService = inject(AuthService);
  constructor(private http: HttpClient) { }

  public getBoards(workspaceId: string | null) : Observable<Board[]> {

    return this.http.get<Board[]>(`${this.apiServerUrl}/${workspaceId}/boards`);
  }
  public getWorkspaceById(workspaceId: string | null) : Observable<Workspace> {
    return this.http.get<Workspace>(`${this.apiServerUrl}/${workspaceId}`);
  }


  public deleteListCard(boardId: number): Observable<void>{
    return this.http.delete<void>(`${this.apiServerUrl}/${boardId}`);
  }
}
