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

  public getBoards(workspaceId: number | null) : Observable<Board[]> {

    return this.http.get<Board[]>(`${this.apiServerUrl}/${workspaceId}/boards`,{withCredentials: true});
  }
  public getWorkspaceById(workspaceId: number | null) : Observable<Workspace> {
    return this.http.get<Workspace>(`${this.apiServerUrl}/${workspaceId}`,{withCredentials: true});
  }
  public deleteWorkspaceById(workspaceId: number) : Observable<Workspace> {
    return this.http.delete<Workspace>(`${this.apiServerUrl}/${workspaceId}/delete`);
  }


  public deleteListCard(boardId: number): Observable<void>{
    return this.http.delete<void>(`${this.apiServerUrl}/${boardId}`);
  }

  public updateWorkspace(workspaceId: number,workspace: Workspace | undefined): Observable<Workspace> {

    return this.http.put<Workspace>(`${this.apiServerUrl}/${workspaceId}/update`,workspace);
  }
  public createBoard(workspaceId: number, board: Board): Observable<Board> {
    return this.http.post<Board>(`${this.apiServerUrl}/${workspaceId}/board`,board)
  }
}
