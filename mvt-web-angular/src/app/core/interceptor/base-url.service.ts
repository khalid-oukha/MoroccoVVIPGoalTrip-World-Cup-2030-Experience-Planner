import { Injectable } from '@angular/core';
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Observable} from 'rxjs';
import * as url from 'node:url';
import {environment} from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class BaseUrlInterceptor implements HttpInterceptor{

  constructor() { }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const isRelativeUrl = !req.url.startsWith('http');

    const clonedRequest = isRelativeUrl
      ? req.clone({ url: `${environment.apiUrl}${req.url}` })
      : req;
    return next.handle(clonedRequest);
  }
}
