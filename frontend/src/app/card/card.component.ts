import {Component, Input} from '@angular/core';

@Component({
  selector: 'app-card',
  standalone: true,
  imports: [],
  template:`<div class="card-content">
    <!--  <div class="status-badges">
        <span class="badge badge-green">d</span>
        <span class="badge badge-yellow">Oct 4</span>
        <span class="badge badge-blue">6/6</span>
      </div>-->
    <div class="task-name">{{ taskName }}</div>
  </div>`,
  styleUrl: './card.component.css'
})
export class CardComponent {
@Input() taskName = "";
}
