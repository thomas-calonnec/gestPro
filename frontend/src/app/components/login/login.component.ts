import {Component, inject, OnInit} from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '@services/auth/auth.service';
import { HttpErrorResponse } from '@angular/common/http';
import { UserService } from '@services/users/user.service';
import { MainService } from '@services/main/main.service';
import { catchError, of, switchMap } from 'rxjs';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatInputModule} from '@angular/material/input';
import {MatSnackBar} from '@angular/material/snack-bar';
import { CookieService } from 'ngx-cookie-service';
import {environment} from '@environments/environment.development';
import {MatButton} from '@angular/material/button';
import {GoogleLoginComponent} from '@components/google-login/google-login.component';
import {MatProgressBar} from '@angular/material/progress-bar';

@Component({
    selector: 'app-login',
  imports: [ReactiveFormsModule, MatFormFieldModule, MatInputModule, MatButton, GoogleLoginComponent, MatProgressBar],
    templateUrl: './login.component.html',
    styleUrl: './login.component.css'
})
export class LoginComponent {
  myForm: FormGroup;
  authService: AuthService = inject(AuthService);
  userService: UserService = inject(UserService);
  router: Router = inject(Router);
  setLoadBar : boolean = false;

  userId: number | null  = 0
  constructor(private fb: FormBuilder) {
    this.myForm = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required]

    });
  }

  private _snackBar = inject(MatSnackBar);

  openSnackBar() {
    if (this.authService.getCurrentUser()) {
      this._snackBar.open('Login accepted!', '', {
        duration: 2000, // Durée d'affichage en millisecondes (ici 3 secondes)
        horizontalPosition: 'center', // Position horizontale : 'start' | 'center' | 'end' | 'left' | 'right'
        verticalPosition: 'bottom', // Position verticale : 'top' | 'bottom'
      });
    }

  }
  loginWithGithub() {
      this.authService.loginGithub()
  }
  onLogin() {

    const username = this.myForm.get('username')?.value;
    const password = this.myForm.get('password')?.value;
    this.setLoadBar = true;
    this.authService.login(username, password).pipe(
      switchMap(() => {
        return this.userService.getUserByUsername(username);
      }),
      catchError((error: HttpErrorResponse) => {
        console.error('Login failed', error);
        this.setLoadBar = false;
        alert("Login failed: your login and/or password is incorrect");
        return of(null); // Renvoyer un observable vide pour éviter des erreurs supplémentaires
      })
    ).subscribe({
      next: (user) => {
        if (user) {
          this.userId = user.id;
          if(this.userId)
              localStorage.setItem("USER_ID",this.userId.toLocaleString())

          this.authService.setCurrentUser(user);
          setTimeout(() => {
            this.router.navigate(['home'])
            //this.router.navigateByUrl(`users/${this.userId}/workspaces`);
         //   window.location.href = `users/${this.authService.getCurrentUser()?.id}/workspaces`;
          },2000)

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

  protected readonly environment = environment;
}

