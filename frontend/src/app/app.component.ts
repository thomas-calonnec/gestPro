import { Component } from '@angular/core';
import { RouterLink, RouterOutlet} from '@angular/router';
import {FormsModule} from '@angular/forms';




@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, RouterLink, FormsModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
  providers: [

  ],
})
export class AppComponent  {


}
