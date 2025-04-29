import {inject} from '@angular/core';
import {AuthService} from '@services/auth/auth.service';
import {Router} from '@angular/router';
import {Observable, tap} from 'rxjs';

export const AuthGuard = (): Observable<boolean> => {
  const authService = inject(AuthService);
  const router = inject(Router);

  return authService.checkAuth().pipe(
    tap((isLoggedIn) => {
      console.log(isLoggedIn)
      if(!isLoggedIn) {
        router.navigate(['/login'])
      }
    })
  )
};
