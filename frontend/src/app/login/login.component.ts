import { Component, inject } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { User } from '../../dao/user';
import {AuthService} from '../auth.service';
import { HttpErrorResponse } from '@angular/common/http';

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
  router : Router = inject(Router);

  constructor(private fb: FormBuilder) {
    this.myForm = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required]

    });
  }

  onSubmit() {

  const username = this.myForm.get('username')?.value;
  const password = this.myForm.get('password')?.value;

   this.authService.login(username,password).subscribe({
    next: (response: {user: User}) => {

      const userId = this.authService.currentUser()?.id;
      console.log(userId)
     // this.router.navigateByUrl(`users/${userId}/workspaces`).then(r => console.log(r))
    },
    error: (error: HttpErrorResponse) => {
      console.error('Login failed ', error);
    }
   })
  }
}
