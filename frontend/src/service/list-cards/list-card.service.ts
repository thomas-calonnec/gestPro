import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Card } from '../../dao/card';
import { Observable } from 'rxjs';
import {environment} from '../../environments/environment.development';

@Injectable({
  providedIn: 'root'
})
export class ListCardService {

  private apiServerUrl = environment.apiUrl + '/listCards';

  constructor(private http: HttpClient) { }

  public createCard(listCardId: number, card : Card): Observable<Card>{
    return this.http.put<Card>(`${this.apiServerUrl}/${listCardId}`,card);
  }

  public getCards(listCardId: number): Observable<Card[]> {
    return this.http.get<Card[]>(`${this.apiServerUrl}/${listCardId}/cards`);
  }
}
