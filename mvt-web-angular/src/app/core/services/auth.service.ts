import { Injectable } from '@angular/core';
import { BehaviorSubject, catchError, Observable, tap, throwError } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { IUser } from '../models/user.model';
import {jwtDecode} from 'jwt-decode';

export interface LoginResponse {
  access_token: string;
  refresh_token: string;
}

const ACCESS_TOKEN_KEY = 'access_token';
const REFRESH_TOKEN_KEY = 'refresh_token';
const USER_DETAILS_KEY = 'userDetails';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private userDetails = new BehaviorSubject<IUser | null>(null);

  constructor(private http: HttpClient) {
    this.loadUserFromStorage();
  }

  login(credentials: { email: string; password: string }): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`/auth/login`, credentials).pipe(
      tap((response: LoginResponse) => {
        this.saveToken(response.access_token, response.refresh_token);
        this.fetchUserDetails().subscribe();
      }),
      catchError((error) => this.handleError(error))
    );
  }

  register(user: { firstname: string; lastname: string; email: string; password: string }): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`/auth/register`, user).pipe(
      tap((response: LoginResponse) => {
        this.saveToken(response.access_token, response.refresh_token);
        this.fetchUserDetails().subscribe();
      }),
      catchError((error) => this.handleError(error))
    )};

  private saveToken(access_token: string, refresh_token: string): void {
    localStorage.setItem(ACCESS_TOKEN_KEY, access_token);
    localStorage.setItem(REFRESH_TOKEN_KEY, refresh_token);
  }

  fetchUserDetails(): Observable<IUser> {
    return this.http.get<IUser>('/auth/me').pipe(
      tap((user) => this.saveUserDetails(user)),
      catchError((error) => this.handleError(error))
    );
  }

  private saveUserDetails(user: IUser): void {
    localStorage.setItem(USER_DETAILS_KEY, JSON.stringify(user));
    this.userDetails.next(user);
  }

  private loadUserFromStorage(): void {
    const storedUser = localStorage.getItem(USER_DETAILS_KEY);
    if (storedUser) {
      this.userDetails.next(JSON.parse(storedUser));
    }
  }

  isAdmin(): boolean {
    const user = this.userDetails.value;
    return !!user && user.roles.includes('ROLE_ADMIN');
  }

  getToken(): string | null {
    return localStorage.getItem(ACCESS_TOKEN_KEY);
  }

  isLoggedIn(): boolean {
    const token = this.getToken();
    return token !== null && !this.isTokenExpired(token);
  }

  private isTokenExpired(token: string): boolean {
    try {
      const decoded: { exp: number } = jwtDecode(token);
      return decoded.exp * 1000 < Date.now();
    } catch (e) {
      return true;
    }
  }

  logout(): void {
    localStorage.removeItem(ACCESS_TOKEN_KEY);
    localStorage.removeItem(REFRESH_TOKEN_KEY);
    localStorage.removeItem(USER_DETAILS_KEY);
    this.userDetails.next(null);
  }

  private handleError(error: any): Observable<never> {
    console.error('AuthService Error:', error);
    return throwError(() => new Error(error.message || 'Something went wrong'));
  }
}
