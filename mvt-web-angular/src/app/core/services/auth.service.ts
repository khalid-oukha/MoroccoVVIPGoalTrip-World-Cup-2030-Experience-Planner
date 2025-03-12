import { Injectable } from '@angular/core';
import { BehaviorSubject, map, Observable, startWith, throwError } from 'rxjs';
import { catchError, switchMap, tap } from 'rxjs/operators';
import { HttpClient } from '@angular/common/http';
import { IUser } from '../models/IUser';

export interface LoginResponse {
  accessToken: string;
  refreshToken: string;
}

export interface Credentials {
  email: string;
  password: string;
}

export interface RegisterData {
  firstName: string;
  lastName: string;
  email: string;
  password: string;
}

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private userDetails = new BehaviorSubject<IUser | null>(null);
  public userDetails$ = this.userDetails.asObservable();
  private currentUser: IUser | null = null;

  constructor(private http: HttpClient) {
    this.initializeUser();
    this.userDetails$.subscribe((user) => {
      this.currentUser = user;
    });
  }

  private initializeUser(): void {
    const token = this.getToken();
    const refreshToken = this.getRefreshToken();

    if (token && refreshToken) {
      this.fetchUserDetails().subscribe();
    }
  }

  login(credentials: Credentials): Observable<IUser> {
    return this.http.post<LoginResponse>('/auth/login', credentials).pipe(
      tap((response) => {
        if (response.accessToken && response.refreshToken) {
          this.saveToken(response.accessToken);
          this.saveRefreshToken(response.refreshToken);
        }
      }),
      switchMap(() => this.fetchUserDetails()),
      catchError((error) => this.handleError(error)),
    );
  }

  register(user: RegisterData): Observable<IUser> {
    return this.http.post<LoginResponse>('/auth/register', user).pipe(
      tap((response) => {
        if (response.accessToken && response.refreshToken) {
          this.saveToken(response.accessToken);
          this.saveRefreshToken(response.refreshToken);
        }
      }),
      switchMap(() => this.fetchUserDetails()),
      catchError((error) => {
        this.clearAuthData();
        return this.handleError(error);
      }),
    );
  }

  fetchUserDetails(): Observable<IUser> {
    return this.http.get<IUser>('/auth/me').pipe(
      tap((user) => this.userDetails.next(user)),
      catchError((error) => this.handleError(error)),
    );
  }

  logout(): void {
    this.clearAuthData();
  }

  private clearAuthData(): void {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
    this.userDetails.next(null);
    this.currentUser = null;
  }

  saveToken(token: string): void {
    if (token) {
      localStorage.setItem('accessToken', token);
    }
  }

  getToken(): string | null {
    return localStorage.getItem('accessToken');
  }

  saveRefreshToken(token: string): void {
    if (token) {
      localStorage.setItem('refreshToken', token);
    }
  }

  getRefreshToken(): string | null {
    return localStorage.getItem('refreshToken');
  }

  getCurrentUser(): IUser | null {
    return this.currentUser;
  }

  isAdmin(): boolean {
    if (!this.currentUser) {
      return false;
    }

    if (this.currentUser.authorities && Array.isArray(this.currentUser.authorities)) {
      return this.currentUser.authorities.includes('ROLE_ADMIN');
    }

    return false;
  }

  isLoggedIn(): boolean {
    return !!this.getToken() && !!this.getRefreshToken();
  }

  isAuthenticated(): Observable<boolean> {
    return this.userDetails$.pipe(
      map((user) => !!user),
      startWith(this.isLoggedIn()),
    );
  }

  private handleError(error: any): Observable<never> {
    if (error.status === 401) {
      this.clearAuthData();
    }
    return throwError(() => new Error(error.message || 'Something went wrong'));
  }
}
