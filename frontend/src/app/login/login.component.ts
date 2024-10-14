import { Component, inject, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { UserService } from '../../service/users/user.service';
import { ActivatedRoute, Router } from '@angular/router';
import { User } from '../../dao/user';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  myForm: FormGroup;
  userId = signal<number>(0);
  userService : UserService = inject(UserService);
  router : Router = inject(Router);

  constructor(private fb: FormBuilder) {
    this.myForm = this.fb.group({
      name: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]]
    });
  }

  onSubmit() {
    if (this.myForm.valid) {
      const email = this.myForm.get('email')?.value;

      const userExist = this.userService.getUserByEmail(email);
      
      userExist.subscribe({ next : (user: User) => {
          this.userId.set(user.id);
          this.router.navigate(['workspace/'])
      }})
    }
  }
}
