import { HttpClient } from '@angular/common/http';
import {Injectable, signal} from '@angular/core';
import { Observable } from 'rxjs';
import { Board } from '@models/board';
import { ListCard } from '@models/list-card';
import {environment} from '@environments/environment.development';

@Injectable({
  providedIn: 'root'
})
export class BoardService {

  private apiServerUrl = environment.apiUrl+'/user/boards';

  constructor(private http: HttpClient) { }


  private _boards = signal<Board[]>([]);
  readonly boards = this._boards.asReadonly();

  public setBoards(newBoards: Board[]){
    this._boards.set(newBoards);
  }
  public fetchBoards(workspaceId: number) {
    this.http.get<Board[]>(`${this.apiServerUrl}/${workspaceId}/boards`,{withCredentials: true}).subscribe({
      next: boardsTab => {
        this.setBoards(boardsTab);

      },
      error: err => {
        console.error('error during boards loading : ',err);
        this.setBoards([]);
      }
    })
  }
  public getBoardById(boardId: number) : Observable<Board> {
    return this.http.get<Board>(`${this.apiServerUrl}/${boardId}`);
  }

  public createBoard(workspaceId: number, board: Board): Observable<Board> {
    return this.http.post<Board>(`${this.apiServerUrl}/${workspaceId}/board`,board)
  }

  public getBoardsByWorkspaceId(workspaceId: number | null) : Observable<Board[]> {
    return this.http.get<Board[]>(`${this.apiServerUrl}/workspace/${workspaceId}`,{withCredentials: true});
  }

  public deleteBoardById(boardId: number) : Observable<Board> {
    return this.http.delete<Board>(`${this.apiServerUrl}/${boardId}`);
  }

  public updateBoard(boardId: number,board: Board) {
    return this.http.put<Board>(`${this.apiServerUrl}/${boardId}/update`,board);
  }

  public getListCards(boardId: number): Observable<ListCard[]>{
    return this.http.get<ListCard[]>(`${this.apiServerUrl}/${boardId}/listCards`,{withCredentials: true});
  }

  public createListCard(boardId: number, listCard : ListCard): Observable<ListCard>{
    return this.http.put<ListCard>(`${this.apiServerUrl}/${boardId}/listCards`,listCard);
  }

  public updateListCard(boardId: number, listCard: ListCard[]): Observable<ListCard[]>{
    return this.http.put<ListCard[]>(`${this.apiServerUrl}/${boardId}/listCards/update`,listCard);
  }

  public deleteListCard(boardId: number): Observable<void>{
    return this.http.delete<void>(`${this.apiServerUrl}/${boardId}`);
  }
}
