import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Card } from '@models/card';
import { Label } from '@models/label';
import {environment} from '@environments/environment.development';
import {CheckList} from '@models/check-list';

@Injectable({
  providedIn: 'root'
})
export class CardService {

  private apiServerUrl = environment.apiUrl+'/user/cards';

  constructor(private http: HttpClient) { }


  public createCard(listCardId: number, card : Card): Observable<Card>{
    return this.http.post<Card>(`${this.apiServerUrl}/listCard/${listCardId}`,card,{withCredentials: true});
  }

  public updateCard(listCardId: number | undefined, card: Card): Observable<Card>{
    return this.http.put<Card>(`${this.apiServerUrl}/${listCardId}/update`,card,{withCredentials : true});
  }

  public updateCheckList(cardId: number | undefined, checkList: CheckList): Observable<Card> {
    return this.http.put<Card>(`${this.apiServerUrl}/${cardId}/check-list/update`,checkList, {withCredentials : true});
  }
  public deleteCard(cardId: number): Observable<void>{
    return this.http.delete<void>(`${this.apiServerUrl}/${cardId}`, {withCredentials : true});
  }

  public addLabelToCard(cardId: number, label: Label): Observable<Card> {
    return this.http.post<Card>(`${this.apiServerUrl}/${cardId}/label/create`,label,{withCredentials : true});
  }

  public removeLabelToCard(cardId: number, label: Label): Observable<Card> {
    return this.http.post<Card>(`${this.apiServerUrl}/${cardId}/label/remove`,label,{withCredentials : true});
  }

}
