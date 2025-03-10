import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PlanService {
  private apiUrl = '/plans';

  constructor(private http: HttpClient) {}

  /**
   * Get all plans with pagination
   */
  getAllPlans(page: number = 0, size: number = 10, sort: string = 'createdAt,desc'): Observable<any> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sort', sort);

    return this.http.get(`${this.apiUrl}`, { params });
  }

  /**
   * Get current user's plans with pagination
   */
  getMyPlans(page: number = 0, size: number = 10, sort: string = 'createdAt,desc'): Observable<any> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sort', sort);

    return this.http.get(`${this.apiUrl}/my-plans`, { params });
  }

  /**
   * Get a specific plan by ID
   */
  getPlanById(id: string): Observable<any> {
    return this.http.get(`${this.apiUrl}/${id}`);
  }

  /**
   * Create a new plan
   */
  createPlan(planData: {name: string, description: string, imageUrl?: string}): Observable<any> {
    return this.http.post(`${this.apiUrl}`, planData);
  }

  /**
   * Update an existing plan
   */
  updatePlan(id: string, planData: {name?: string, description?: string, imageUrl?: string}): Observable<any> {
    return this.http.put(`${this.apiUrl}/${id}`, planData);
  }

  /**
   * Delete a plan
   */
  deletePlan(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  /**
   * Add an activity to a plan
   */
  addActivityToPlan(planId: string, activityId: string, plannedActivityData: {
    priority?: string,  // Update to include priority
    startDate?: string,
    endDate?: string,
    notes?: string
  }): Observable<void> {
    return this.http.post<void>(
      `${this.apiUrl}/${planId}/activities/${activityId}`,
      plannedActivityData
    );
  }

  /**
   * Remove an activity from a plan
   */
  removeActivityFromPlan(planId: string, activityId: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${planId}/activities/${activityId}`);
  }
}
