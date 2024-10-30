import { Component, inject } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { User } from '../../dao/user';
import {AuthService} from '../auth.service';
import {HttpErrorResponse, HttpHeaders} from '@angular/common/http';
import {UserService} from '../../service/users/user.service';

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
  router : Router = inject(Router);

  constructor(private fb: FormBuilder) {
    this.myForm = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required]

    });
  }

  onLogin() {

  const username = this.myForm.get('username')?.value;
  const password = this.myForm.get('password')?.value;

   this.authService.login(username,password).subscribe({
    next: (response) => {

      //const userId = response.id
     // console.log(userId)
      //const headers = new HttpHeaders().set('Authorization', `Bearer ${this.authService.getToken()}`);

      this.router.navigateByUrl(`users/1/workspaces`).then(r => console.log(r))
    },
    error: (error: HttpErrorResponse) => {
      console.error('Login failed ', error);
      alert("Login failed : " + error.message);
    }
   })
  }
}
