import { Component, inject } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../auth.service';
import { HttpErrorResponse } from '@angular/common/http';
import { UserService } from '../../../service/users/user.service';
import { MainService } from '../../../service/main/main.service';
import { catchError, of, switchMap } from 'rxjs';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  myForm: FormGroup;
  authService: AuthService = inject(AuthService);
  userService: UserService = inject(UserService);
  mainService: MainService = inject(MainService);
  router: Router = inject(Router);

  userId: number = 0
  constructor(private fb: FormBuilder) {
    this.myForm = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required]

    });
  }

  onLogin() {

    const username = this.myForm.get('username')?.value;
    const password = this.myForm.get('password')?.value;

    this.authService.login(username, password).pipe(
      switchMap(() => {
        return this.userService.getUserByUsername(username);
      }),
      catchError((error: HttpErrorResponse) => {
        console.error('Login failed', error);
        alert("Login failed: " + error.message);
        return of(null); // Renvoyer un observable vide pour éviter des erreurs supplémentaires
      })
    ).subscribe({
      next: (user) => {
        if (user) {
          this.userId = user.id;
          this.router.navigateByUrl(`users/${this.userId}/workspaces`).then(r => console.log(r));
        }
      }
    });
    /* this.authService.login(username,password).subscribe({
      next: () => {
  
        console.log("test")
        this.userService.getUserByUsername(username).subscribe({
          next: (user) => {
            this.userId = user.id;
  
            this.router.navigateByUrl(`users/${this.userId}/workspaces`).then(r => console.log(r))
          }
        })
       // console.log(userId)
        //const headers = new HttpHeaders().set('Authorization', `Bearer ${this.authService.getToken()}`);
  
  
      },
      error: (error: HttpErrorResponse) => {
        console.error('Login failed ', error);
  
        alert("Login failed : " + error.message);
      }
     })
    }*/
  }

}

