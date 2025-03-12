import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { City } from '../models/City';

@Injectable({
  providedIn: 'root'
})
export class CitiesService {
  private apiUrl = '/city';

  constructor(private http: HttpClient) { }

  findAll(region?: string): Observable<City[]> {
    const url = region ? `${this.apiUrl}?region=${region}` : this.apiUrl;
    return this.http.get<City[]>(url);
  }

  create(city: City): Observable<City> {
    return this.http.post<City>(this.apiUrl, city);
  }

  update(id: number, city: City): Observable<City> {
    return this.http.put<City>(`${this.apiUrl}/${id}`, city);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
