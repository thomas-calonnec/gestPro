import {Component, signal} from '@angular/core';
import { RouterLink, RouterOutlet} from '@angular/router';
import {FormsModule} from '@angular/forms';
import {FontAwesomeModule} from '@fortawesome/angular-fontawesome';
import {AuthService} from '@services/auth/auth.service';
import {LoadingComponent} from '@app/src/app/components/loading/loading.component';




@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, FormsModule, FontAwesomeModule, LoadingComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss',
  providers: [

  ],
})
export class AppComponent  {

  loading = signal(false);

  constructor(private auth: AuthService) {
    // checkAuth() returns an Observable
   if (!this.auth.isConnected()) {

     // setTimeout(() => this.loading.set(false),150)
    }
  }

}
