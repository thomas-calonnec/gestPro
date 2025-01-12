import {Injectable} from '@angular/core';
import { Observable } from 'rxjs';
import { Board } from '@models/board';
import {HttpClient} from '@angular/common/http';
import {environment} from '@environments/environment.development';
import {Workspace} from '@models/workspace';


@Injectable({
  providedIn: 'root'
})
export class WorkspaceService {

  private apiServerUrl = environment.apiUrl + '/user/workspaces';
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

  public createBoard(workspaceId: string | null, board: Board): Observable<Board> {
    return this.http.post<Board>(`${this.apiServerUrl}/${workspaceId}/board`,board)
  }
}
