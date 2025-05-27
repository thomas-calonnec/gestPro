import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Card } from '@models/card';
import {Observable} from 'rxjs';
import {environment} from '@environments/environment.development';
import {ListCard} from '@models/list-card';

@Injectable({
  providedIn: 'root'
})
export class ListCardService {

  private apiServerUrl = environment.apiUrl + '/user/listCards';

  constructor(private http: HttpClient) { }

  public getCards(listCardId: number): Observable<Card[]> {
    return this.http.get<Card[]>(`${this.apiServerUrl}/${listCardId}/cards`,{withCredentials: true});
  }

  public getListCards(boardId: number): Observable<ListCard[]>{
    return this.http.get<ListCard[]>(`${this.apiServerUrl}/board/${boardId}`,{withCredentials: true});
  }

  public createListCard(boardId: number, listCard : ListCard): Observable<ListCard>{
    return this.http.post<ListCard>(`${this.apiServerUrl}/board/${boardId}`,listCard,{withCredentials: true});
  }

  public updateListCard(boardId: number, listCard: ListCard[]): Observable<ListCard[]>{
    return this.http.put<ListCard[]>(`${this.apiServerUrl}/board/${boardId}`,listCard,{withCredentials: true});
  }

  public deleteListCard(boardId: number): Observable<void>{
    return this.http.delete<void>(`${this.apiServerUrl}/board/${boardId}`,{withCredentials: true});
  }
}
