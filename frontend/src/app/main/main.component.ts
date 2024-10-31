import { Component, inject, OnInit} from '@angular/core';
import {Router, RouterLink, RouterOutlet} from "@angular/router";
import {AuthService} from '../auth.service';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-main',
  standalone: true,
    imports: [
        RouterLink,
        RouterOutlet,
      FormsModule
    ],
  templateUrl: './main.component.html',
  styleUrl: './main.component.css'
})
export class MainComponent implements OnInit {

  token: String = "";
  authService : AuthService = inject(AuthService);

  router : Router = inject(Router);
  loggedIn = true

  ngOnInit() {

    this.loggedIn = true;
  }

  logout() {
    this.loggedIn = false;
    this.authService.logout();
  }
}
