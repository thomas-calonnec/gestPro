import { HttpClient } from '@angular/common/http';
import {Injectable} from '@angular/core';
import { Observable } from 'rxjs';
import { Board } from '@/models/board';
import { ListCard } from '@/models/list-card';
import {environment} from '@/environments/environment.development';

@Injectable({
  providedIn: 'root'
})
export class BoardService {

  private apiServerUrl = environment.apiUrl+'/user/boards';

  constructor(private http: HttpClient) { }

  public getBoardById(boardId: number) : Observable<Board> {
    return this.http.get<Board>(`${this.apiServerUrl}/${boardId}`);
  }

  public getListCards(boardId: number): Observable<ListCard[]>{
    return this.http.get<ListCard[]>(`${this.apiServerUrl}/${boardId}/listCards`);
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
