import {inject} from '@angular/core';
import {AuthService} from '@services/auth/auth.service';
import {ActivatedRoute, Router} from '@angular/router';
import {UserService} from '@services/users/user.service';
import {catchError, map, Observable} from 'rxjs';

export const AuthGuard   = (): Observable<boolean> => {

    const authService = inject(AuthService);
    const router : Router = inject(Router);
    return authService.isAuthenticated().pipe(
      map((isAuthenticated) => {
        if (isAuthenticated) {
          return true; // L'utilisateur est authentifié
        } else {
          router.navigate(['/login']); // Rediriger l'utilisateur vers la page de connexion
          return false;
        }
      }),
      catchError(() => {
        // En cas d'erreur (par exemple, problème de cookie), rediriger vers la page de connexion
        router.navigate(['/login']);
        return [false];
      })
    );




}
