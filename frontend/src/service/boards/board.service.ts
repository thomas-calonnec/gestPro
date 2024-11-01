import { HttpClient } from '@angular/common/http';
import {Injectable, signal, WritableSignal} from '@angular/core';
import { Observable } from 'rxjs';
import { Board } from '../../dao/board';
import { ListCard } from '../../dao/list-card';

@Injectable({
  providedIn: 'root'
})
export class BoardService {

  private apiServerUrl = 'api/boards';

  private boards : WritableSignal<Board[]> = signal<Board[]>([]);

  constructor(private http: HttpClient) { }

  getBoards() {
    return this.boards.asReadonly();
  }

  setBoards(boards: Board[]) {
    this.boards.set(boards);
  }

  public getBoardById(boardId: number) : Observable<Board> {
    return this.http.get<Board>(`${this.apiServerUrl}/${boardId}`);
  }

  public getListCards(boardId: number): Observable<ListCard[]>{
    return this.http.get<ListCard[]>(`${this.apiServerUrl}/${boardId}/listCards`);
  }

  public createListCard(boardId: number, listCard : ListCard): Observable<ListCard>{
    return this.http.put<ListCard>(`${this.apiServerUrl}/${boardId}`,listCard);
  }

  public updateListCard(boardId: number, listCard: ListCard): Observable<ListCard>{
    return this.http.put<ListCard>(`${this.apiServerUrl}/${boardId}`,listCard);
  }

  public deleteListCard(boardId: number): Observable<void>{
    return this.http.delete<void>(`${this.apiServerUrl}/${boardId}`);
  }
}
