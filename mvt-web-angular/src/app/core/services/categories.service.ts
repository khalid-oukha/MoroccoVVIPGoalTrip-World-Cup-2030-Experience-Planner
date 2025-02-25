import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Category } from '../models/Category';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class CategoriesService {
  private apiUrl = '/categories';

  constructor(private http: HttpClient) {}

  findAll(page: number = 0, size: number = 10, sort: string = 'name,asc'): Observable<Category[]> {
    return this.http.get<Category[]>(`${this.apiUrl}?page=${page}&size=${size}&sort=${sort}`);
  }

  create(formData: FormData): Observable<Category> {
    return this.http.post<Category>(`${this.apiUrl}`, formData);
  }

  update(id: number | undefined, categoryData: FormData): Observable<Category> {
    return this.http.patch<Category>(`${this.apiUrl}/${id}`, categoryData);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
