import { Injectable } from '@angular/core';
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Observable} from 'rxjs';
import {AuthService} from '../services/auth.service';

@Injectable({
  providedIn: 'root'
})
export class AuthorizationHeaderService implements HttpInterceptor{

  constructor(private authService: AuthService) { }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const access_token = this.authService.getToken()
    if (access_token) {
      const clonedRequest = req.clone({
        setHeaders: {
          Authorization: `Bearer ${access_token}`,
        },
      })

      return next.handle(clonedRequest);
    }

    return next.handle(req);
  }
}
