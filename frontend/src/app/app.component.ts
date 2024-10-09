import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-root',
 
  template: `
<router-outlet />
`,
standalone: true,
imports: [RouterOutlet],
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'gestPro';
}
