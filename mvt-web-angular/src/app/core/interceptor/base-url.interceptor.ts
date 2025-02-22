import { HttpInterceptorFn } from '@angular/common/http';
import { environment } from '../../../environments/environment';

export const baseUrlInterceptor: HttpInterceptorFn = (req, next) => {
  const isRelativeUrl = !req.url.startsWith('http');

  const clonedRequest = isRelativeUrl
    ? req.clone({ url: `${environment.apiUrl}${req.url}` })
    : req;

  return next(clonedRequest);
};
