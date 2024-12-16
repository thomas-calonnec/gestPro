import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Card } from '../../dao/card';
import { Label } from '../../dao/label';
import {environment} from '../../environments/environment.development';

@Injectable({
  providedIn: 'root'
})
export class CardService {

  private apiServerUrl = environment.apiUrl+'/user/cards';

  constructor(private http: HttpClient) { }

  public updateCard(listCardId: number, card: Card): Observable<Card>{

    return this.http.put<Card>(`${this.apiServerUrl}/${listCardId}/update`,card);
  }

  public deleteCard(cardId: number): Observable<void>{
    return this.http.delete<void>(`${this.apiServerUrl}/${cardId}`);
  }

  public addLabelToCard(cardId: number, label: Label): Observable<Card> {
    return this.http.post<Card>(`${this.apiServerUrl}/${cardId}`,label);
  }

  public removeLabelToCard(cardId: number, label: Label): Observable<ArrayBuffer> {
     // @ts-ignore
    return this.http.delete<void>(`${this.apiServerUrl}/${cardId}`,label);
  }

}
