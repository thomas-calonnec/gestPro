import { Injectable } from '@angular/core';
import {environment} from '@environments/environment.development';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Label} from '@models/label';

@Injectable({
  providedIn: 'root'
})
export class LabelService {

  private apiServerUrl = environment.apiUrl+'/user/labels';

  constructor(private http: HttpClient) { }

  public getLabels(): Observable<Label[]>{
    return this.http.get<Label[]>(`${this.apiServerUrl}`);
  }

}
