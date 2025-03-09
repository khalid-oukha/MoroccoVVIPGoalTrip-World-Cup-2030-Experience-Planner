import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Activity } from '../models/Activity';
import { Page } from '../models/Page';
@Injectable({
  providedIn: 'root'
})
export class ActivitesService {
  private apiUrl = '/activities';

  constructor(private http: HttpClient) { }

  findAll(
    cityId?: number,
    categoryId?: number,
    available?: boolean,
    search?: string,
    page: number = 0,
    size: number = 10,
    sort: string = 'id,asc'
  ): Observable<Page<Activity>> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sort', sort);

    if (cityId) params = params.set('cityId', cityId.toString());
    if (categoryId) params = params.set('categoryId', categoryId.toString());
    if (available !== undefined) params = params.set('available', available.toString());

    const trimmedSearch = search?.trim();
    if (trimmedSearch) {
      params = params.set('search', encodeURIComponent(trimmedSearch));
    }

    return this.http.get<Page<Activity>>(this.apiUrl, { params });
  }

  findById(id: string): Observable<Activity> {
    return this.http.get<Activity>(`${this.apiUrl}/${id}`);
  }

  create(formData: FormData): Observable<Activity> {
    return this.http.post<Activity>(this.apiUrl, formData);
  }

  update(id: string, formData: FormData): Observable<Activity> {
    return this.http.put<Activity>(`${this.apiUrl}/${id}`, formData);
  }

  delete(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  getTopActivities(limit: number = 6): Observable<Activity[]> {
    const params = new HttpParams().set('limit', limit.toString());
    return this.http.get<Activity[]>(`${this.apiUrl}/top`, { params });
  }

}
