import {inject, Injectable} from '@angular/core';
import {
  HttpErrorResponse,
  HttpEvent, HttpHandler, HttpHandlerFn,
  HttpInterceptor,
  HttpRequest
} from '@angular/common/http';
import {catchError, Observable, switchMap, tap, throwError} from 'rxjs';
import {AuthService} from '../../app/auth.service';
import {Router} from '@angular/router';
import {JwtHelperService} from  '@auth0/angular-jwt';

@Injectable({
  providedIn: 'root'
})
export class AuthInterceptor implements HttpInterceptor {

  private authService: AuthService = inject(AuthService)
  private jwtHelper = new JwtHelperService();
  private router : Router = inject(Router);
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

}
