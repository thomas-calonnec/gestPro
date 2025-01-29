import { Component, OnInit, inject } from '@angular/core';
import { Card } from '../../dao/card';
import { ListCardService } from '@services/list-cards/list-card.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-list-card',
  standalone: true,
  imports: [],
  template: `@for (card of cards; track card.cardId) {}`,
  styleUrl: './list-card.component.css'
})
export class ListCardComponent implements OnInit {

  public cards : Card[] = [];
  private listCardId : number = 0;

  private listCardService : ListCardService = inject(ListCardService);

  constructor(private route: ActivatedRoute) {}

  ngOnInit(): void {
    this.listCardId = this.route.snapshot.params['id'];
    this.getListCard(this.listCardId);
  }

  public getListCard(listCardId : number) : void{

  }
}
