import {Component, OnInit} from '@angular/core';
import {RouterLink, RouterOutlet} from '@angular/router';
import {AuthService} from './auth.service';


@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, RouterLink],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit {
  loggedIn: boolean = false;

  constructor(private authService: AuthService) {}

  ngOnInit(): void {
    // S'abonner à l'état de connexion et mettre à jour la variable loggedIn
    this.authService.loggedIn$.subscribe((isLoggedIn) => {
      this.loggedIn = isLoggedIn;
    });
  }

}
