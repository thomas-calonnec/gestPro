import {inject, Injectable} from '@angular/core';
import {
  HttpErrorResponse,
  HttpEvent, HttpHandler,
  HttpInterceptor,
  HttpRequest
} from '@angular/common/http';
import {catchError, Observable, switchMap, throwError} from 'rxjs';
import {AuthService} from '@services/auth/auth.service';

@Injectable({
  providedIn: 'root'
})
export class AuthInterceptor implements HttpInterceptor {

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const authService = inject(AuthService);

    return next.handle(req).pipe(
      catchError((error: HttpErrorResponse) => {
        if (error.status === 401) {
          return authService.revokeToken().pipe(
            switchMap(() => {
              return next.handle(req);
            }),
            catchError((refreshError) => {
              // Si le rafraîchissement échoue, déconnectez l'utilisateur
              authService.logout();
              return throwError(() => refreshError);
            })
          );
        }
        return throwError(() => error);
      })
    );
  }
  /*  const accessToken = this.authService.getAccessToken();

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
  }*/
}
