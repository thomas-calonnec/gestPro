import {inject, Injectable} from '@angular/core';
import {
  HttpEvent, HttpHandler,
  HttpInterceptor,
  HttpRequest
} from '@angular/common/http';
import {catchError, Observable, switchMap, throwError} from 'rxjs';
import {AuthService} from '@app/auth.service';

@Injectable({
  providedIn: 'root'
})
export class AuthInterceptor implements HttpInterceptor {

  private authService: AuthService = inject(AuthService)

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const accessToken = this.authService.getAccessToken();

    if (accessToken) {
      req = req.clone({
        setHeaders: {
          Authorization: `Bearer ${accessToken}`
        }
      });
      return next.handle(req);
    }

    return next.handle(req).pipe(
      catchError(error => {
        if (error.status === 401 ) { // Unauthorized, token expired
          return this.authService.refreshAccessToken().pipe(
            switchMap(() => {
              const accessToken = this.authService.getAccessToken();

              req = req.clone({
                setHeaders: {
                  Authorization: `Bearer ${accessToken}`
                }
              });
              return next.handle(req);
            })
          );
        }
        return throwError(() =>error);
      })
    );
  }
}
