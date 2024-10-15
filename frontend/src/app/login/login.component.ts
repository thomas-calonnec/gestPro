import { Component, inject, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { UserService } from '../../service/users/user.service';
import { ActivatedRoute, Router } from '@angular/router';
import { User } from '../../dao/user';
import {environment} from '../../environments/environment.development';

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
         console.log(user)
          this.router.navigateByUrl(`workspaces/${user.userId}/boards`).then(r => console.log(r));
      }})
    }
  }
}
