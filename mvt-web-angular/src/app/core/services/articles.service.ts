import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Article } from '../models/Article';
import { Page } from '../models/Page';

@Injectable({
  providedIn: 'root'
})
export class ArticlesService {
  private apiUrl = '/articles';

  constructor(private http: HttpClient) { }

  findAll(
    search?: string,
    page: number = 0,
    size: number = 10,
    sort: string = 'createdAt,desc'
  ): Observable<Page<Article>> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sort', sort);

    if (search?.trim()) {
      params = params.set('search', encodeURIComponent(search.trim()));
    }

    return this.http.get<Page<Article>>(this.apiUrl, { params });
  }

  findById(id: string): Observable<Article> {
    return this.http.get<Article>(`${this.apiUrl}/${id}`);
  }

  create(formData: FormData): Observable<Article> {
    return this.http.post<Article>(this.apiUrl, formData);
  }

  update(id: string, formData: FormData): Observable<Article> {
    return this.http.put<Article>(`${this.apiUrl}/${id}`, formData);
  }

  delete(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  prepareFormData(articleData: any): FormData {
    const formData = new FormData();

    if (articleData.content) {
      formData.append('content', articleData.content);
    }

    if (articleData.activityId) {
      formData.append('activityId', articleData.activityId);
    }

    if (articleData.image && articleData.image instanceof File) {
      formData.append('image', articleData.image);
    }

    return formData;
  }
}
