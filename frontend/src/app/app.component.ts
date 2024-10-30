import {Component, inject, OnInit, signal, Signal} from '@angular/core';
import {RouterLink, RouterOutlet} from '@angular/router';
import {AuthService} from './auth.service';
import {User} from '../dao/user';


@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, RouterLink],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit {
 islogged : Signal<boolean> = signal<boolean>(false);
 currentUser: Signal<User | null> = signal<User | null>(null)
  token: String = "";
 authService : AuthService = inject(AuthService);

 ngOnInit() {
  this.islogged = this.authService.isConnected;
  this.currentUser = this.authService.currentUser;
 }

}
