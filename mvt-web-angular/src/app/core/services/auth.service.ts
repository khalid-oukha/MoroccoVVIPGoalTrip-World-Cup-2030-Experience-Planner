import { Injectable } from '@angular/core';
import { BehaviorSubject, catchError, Observable, tap, throwError } from 'rxjs';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import { IUser } from '../models/user.model';
import {jwtDecode} from 'jwt-decode';

export interface LoginResponse {
  accessToken: string;
  refreshToken: string;
}

const ACCESS_TOKEN_KEY = 'access_token';
const REFRESH_TOKEN_KEY = 'refresh_token';
const USER_DETAILS_KEY = 'userDetails';
const API_URL = 'http://localhost:8080/api/v1'; // Add base API URL

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private userDetails = new BehaviorSubject<IUser | null>(null);

  constructor(private http: HttpClient) {
    this.loadUserFromStorage();
  }

  login(credentials: { email: string; password: string }): Observable<LoginResponse> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post<LoginResponse>(`/api/v1/auth/login`, credentials, { headers }).pipe(
      tap((response: LoginResponse) => {
        console.log('Login response:', response);
        localStorage.setItem(ACCESS_TOKEN_KEY, response.accessToken);
        localStorage.setItem(REFRESH_TOKEN_KEY, response.refreshToken);
        this.fetchUserDetails().subscribe();
      }),
      catchError((error) => this.handleError(error))
    );
  }

  register(user: { firstname: string; lastname: string; email: string; password: string }): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${API_URL}/auth/register`, user).pipe(
      tap((response: LoginResponse) => {
        localStorage.setItem(ACCESS_TOKEN_KEY, response.accessToken);
        localStorage.setItem(REFRESH_TOKEN_KEY, response.refreshToken);
        this.fetchUserDetails().subscribe();
      }),
      catchError((error) => this.handleError(error))
    );
  }

  fetchUserDetails(): Observable<IUser> {
    return this.http.get<IUser>(`${API_URL}/auth/me`).pipe(
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
    let errorMessage = 'Something went wrong';

    if (error.status === 0) {
      errorMessage = 'Cannot connect to the server. Check your network or backend status.';
    } else if (error.status === 401) {
      errorMessage = 'Invalid email or password';
    }

    console.error('AuthService Error:', error);
    return throwError(() => new Error(errorMessage));
  }
}
