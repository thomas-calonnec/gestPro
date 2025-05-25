import {Injectable} from '@angular/core';
import { Observable } from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {environment} from '@environments/environment.development';
import {Workspace} from '@models/workspace';


@Injectable({
  providedIn: 'root'
})
export class WorkspaceService {

  private apiServerUrl = environment.apiUrl + '/user/workspaces';
  constructor(private http: HttpClient) { }


  public createWorkspace(userId: number, workspace: Workspace): Observable<Workspace>{
    return this.http.post<Workspace>(`${this.apiServerUrl}/${userId}/workspace`,workspace);
  }

  public getWorkspaceById(workspaceId: number | null) : Observable<Workspace> {
    return this.http.get<Workspace>(`${this.apiServerUrl}/${workspaceId}`,{withCredentials: true});
  }

  public getWorkspaces(id: string): Observable<Workspace[]>{
    return this.http.get<Workspace[]>(`${this.apiServerUrl}/${id}/workspaces`,{withCredentials: true});
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

}
