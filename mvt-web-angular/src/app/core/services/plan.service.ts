import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import {tap} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class PlanService {
  private apiUrl = '/plans';

  constructor(private http: HttpClient) {}

  getAllPlans(page: number = 0, size: number = 10, sort: string = 'createdAt,desc'): Observable<any> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sort', sort);

    return this.http.get(`${this.apiUrl}`, { params });
  }

  getMyPlans(page: number = 0, size: number = 10, sort: string = 'createdAt,desc'): Observable<any> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sort', sort);

    return this.http.get(`${this.apiUrl}/my-plans`, { params });
  }

  getPlanById(id: string): Observable<any> {
    return this.http.get(`${this.apiUrl}/${id}`);
  }

  createPlan(planData: FormData): Observable<any> {
    return this.http.post(`${this.apiUrl}`, planData);
  }

  updatePlan(id: string, planData: FormData): Observable<any> {
    return this.http.put(`${this.apiUrl}/${id}`, planData);
  }

  deletePlan(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  addActivityToPlan(planId: string, activityId: string, plannedActivityData: {
    priority?: string,
    startDate?: string,
    endDate?: string,
    notes?: string
  }): Observable<void> {
    return this.http.post<void>(
      `${this.apiUrl}/${planId}/activities/${activityId}`,
      plannedActivityData
    );
  }

  deletePlannedActivity(planId: string, plannedActivityId: string): Observable<void> {
    return this.http.delete<void>(
      `${this.apiUrl}/${planId}/planned-activities/${plannedActivityId}`
    );
  }


  updatePlannedActivity(planId: string, plannedActivityId: string, updateData: {
    priority?: string,
    startDate?: string,
    endDate?: string,
    notes?: string
  }): Observable<any> {
    return this.http.put(
      `${this.apiUrl}/${planId}/planned-activities/${plannedActivityId}`,
      updateData
    );
  }
}
