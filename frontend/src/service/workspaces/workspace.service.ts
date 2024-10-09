import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Board } from '../../dao/board';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class WorkspaceService {

  private apiServerUrl = 'api/workspaces';

  constructor(private http: HttpClient) { }

  public getBoards() : Observable<Board[]> {
    return this.http.get<Board[]>(`${this.apiServerUrl}`);
  }
  
  public deleteListCard(boardId: number): Observable<void>{
    return this.http.delete<void>(`${this.apiServerUrl}/${boardId}`);
  }
}
