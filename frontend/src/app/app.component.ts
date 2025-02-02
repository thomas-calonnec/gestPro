import { Component } from '@angular/core';
import { RouterLink, RouterOutlet} from '@angular/router';
import {FormsModule} from '@angular/forms';
import {FontAwesomeModule} from '@fortawesome/angular-fontawesome';




@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, FormsModule, FontAwesomeModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss',
  providers: [

  ],
})
export class AppComponent  {

  title = "test"
  autoResize(textarea: HTMLTextAreaElement): void {
    textarea.style.height = 'auto';
    textarea.style.height = textarea.scrollHeight + 'px';
  }

}
