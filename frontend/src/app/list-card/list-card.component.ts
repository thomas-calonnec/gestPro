import {Component, OnInit, inject, Signal, signal, Input, WritableSignal} from '@angular/core';
import { Card } from '../../dao/card';
import { ListCardService } from '../../service/list-cards/list-card.service';
import { ActivatedRoute } from '@angular/router';
import {CardComponent} from '../card/card.component';

@Component({
  selector: 'app-list-card',
  standalone: true,
  imports: [
    CardComponent
  ],
  template: `
    <div class="card-container">
      <div class="card">
        <div class="card-header">
          <span class="title">{{ title }}</span>
          <div class="icons">
            <i class="fa-solid fa-arrows-up-down-left-right"></i> <!-- Icône de déplacement -->
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
  public cards : WritableSignal<Card[]> = signal<Card[]>([]);
  private listCardId : number = 0;
  public listCardService = inject(ListCardService);
  private route : ActivatedRoute = inject(ActivatedRoute);

  ngOnInit(): void {
    this.listCardId = this.route.snapshot.params['id'];
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
