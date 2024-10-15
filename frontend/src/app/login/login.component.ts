import { Component, inject } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { UserService } from '../../service/users/user.service';
import { Router } from '@angular/router';
import { User } from '../../dao/user';
import {AuthService} from '../auth.service';

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

    if (this.myForm.valid) {
      const email = this.myForm.get('email')?.value;

      const userExist = this.userService.getUserByEmail(email);

      userExist.subscribe({ next : (user: User) => {
          this.authService.setLoggedIn(true);
          this.router.navigateByUrl(`users/${user.userId}/workspaces`).then(r => console.log(r));
      }})
    }
  }
}
