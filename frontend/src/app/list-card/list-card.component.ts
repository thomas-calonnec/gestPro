import {Component, OnInit, inject, signal, Input, WritableSignal} from '@angular/core';
import { Card } from '../../dao/card';
import { ListCardService } from '../../service/list-cards/list-card.service';
import {CardComponent} from '../card/card.component';
import {HorizontalDragDropExampleComponent} from '../horizontal/horizontal.component';

@Component({
  selector: 'app-list-card',
  standalone: true,
  imports: [
    CardComponent,
    HorizontalDragDropExampleComponent
  ],
  template: `

    <div class="card-container">
      <div class="card">
        <div class="card-header">
          <span class="title">{{ title }}</span>
          <div class="icons">
            <i class="fa-solid fa-ellipsis"></i> <!-- Icône de menu -->
          </div>
        </div>
        @for (card of cards(); track card.id) {
          <app-card [taskName]="card.name"> </app-card>
        }

        <div class="card-footer">
          <div style="margin-right: 10px;"> <i class="fa-solid fa-circle-plus"></i></div>  Add a card
        </div>
      </div>
    </div>
    <!--@for (card of cards(); track card.id) {
    <p>{{card.name}}</p>
  }-->`,
  styleUrl: './list-card.component.css'
})
export class ListCardComponent implements OnInit {
  @Input() title = '';
  @Input() listCardId = 0;
  public cards : WritableSignal<Card[]> = signal<Card[]>([]);

  public listCardService = inject(ListCardService);

  ngOnInit(): void {
    this.getListCard(this.listCardId);

  }

  public getListCard(listCardId : number) : void{
    this.listCardService.getCards(listCardId).subscribe({
      next: (data: Card[]) => {
        this.cards.set(data);
      }
    })
  }
}
