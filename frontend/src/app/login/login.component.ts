import { Component, inject } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { UserService } from '../../service/users/user.service';
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
  userService : UserService = inject(UserService);
  authService: AuthService = inject(AuthService);
  router : Router = inject(Router);

  constructor(private fb: FormBuilder) {
    this.myForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required]

    });
  }

  onSubmit() {
    
  const email = this.myForm.get('email')?.value;
  const password = this.myForm.get('password')?.value;
    
   this.authService.login(email,password).subscribe({
    next: (response : {user: User}) => {
      console.log('Logged in successfully');
      this.router.navigateByUrl(`user/${response.user.userId}/workspaces`)
    },
    error: (error: HttpErrorResponse) => {
      console.error('Login failed ', error);
    }
   })
  }
}
