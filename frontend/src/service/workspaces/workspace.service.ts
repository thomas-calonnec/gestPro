import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Board } from '../../dao/board';
import { HttpClient } from '@angular/common/http';
import {environment} from '../../environments/environment.development';


@Injectable({
  providedIn: 'root'
})
export class WorkspaceService {

  private apiServerUrl = environment.apiUrl + '/workspaces';

  constructor(private http: HttpClient) { }

  public getBoards(workspaceId: string | null) : Observable<Board[]> {
    return this.http.get<Board[]>(`${this.apiServerUrl}/${workspaceId}/boards`);
  }


  public deleteListCard(boardId: number): Observable<void>{
    return this.http.delete<void>(`${this.apiServerUrl}/${boardId}`);
  }
}
