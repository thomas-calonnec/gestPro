import {inject} from '@angular/core';
import {AuthService} from './auth.service';
import {Router} from '@angular/router';

export const AuthGuard  = () => {
  const auth = inject(AuthService);
  const router = inject(Router);

  if(!auth.isAuthenticated()) {
    router.navigateByUrl('/login').then(r => r);
    return false;
  }
  return true;


}
