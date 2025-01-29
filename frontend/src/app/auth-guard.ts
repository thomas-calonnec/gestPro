import {inject} from '@angular/core';
import {AuthService} from '@services/auth/auth.service';
import {Router} from '@angular/router';

export const AuthGuard  = () => {
  const auth = inject(AuthService);
  const router = inject(Router);
console.log("auth : " + auth.isAuthenticated())

  return auth.isAuthenticated().subscribe({
    next: response => {
      if(!response){
        router.navigateByUrl('login').then(r => console.log(r))
      }
    }
  })


}
