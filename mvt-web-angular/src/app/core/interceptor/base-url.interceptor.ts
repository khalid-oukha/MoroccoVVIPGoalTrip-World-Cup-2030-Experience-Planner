import { Injectable } from '@angular/core';
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Observable} from 'rxjs';
import {environment} from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class BaseUrlInterceptor implements HttpInterceptor{

  constructor() {
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const isRelativeUrl = !req.url.startsWith('http');

    if (req.url.includes('assets')) {
      return next.handle(req);
    }
    const apiReq = isRelativeUrl
      ? req.clone({url: `${environment.apiUrl}${req.url}`})
      : req;
    console.log('Modified request URL:', apiReq.url); // Add this line

    return next.handle(apiReq);
  }
}
