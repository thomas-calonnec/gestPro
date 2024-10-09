import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Card } from '../../dao/card';
import { Label } from '../../dao/label';

@Injectable({
  providedIn: 'root'
})
export class CardService {

  private apiServerUrl = 'api/cards';

  constructor(private http: HttpClient) { }

  public updateCard(listCardId: number, card: Card): Observable<Card>{
    return this.http.put<Card>(`${this.apiServerUrl}/${listCardId}`,card);
  }
  
  public deletCard(cardId: number): Observable<void>{
    return this.http.delete<void>(`${this.apiServerUrl}/${cardId}`);
  }

  public addLabelToCard(cardId: number, label: Label): Observable<Card> {
    return this.http.post<Card>(`${this.apiServerUrl}/${cardId}`,label);
  }

  public removeLabelToCard(cardId: number, label: Label): Observable<void> {
    return this.http.delete<void>(`${this.apiServerUrl}/${cardId}`,label);
  }

}
