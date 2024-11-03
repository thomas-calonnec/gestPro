import {Component, inject, OnInit, signal, WritableSignal} from '@angular/core';
import { BoardService } from '../../service/boards/board.service';
import {ListCardComponent} from '../list-card/list-card.component';
import {ActivatedRoute, RouterLink} from '@angular/router';
import {FaIconComponent} from '@fortawesome/angular-fontawesome';
import {ListCard} from '../../dao/list-card';
@Component({
  selector: 'app-board',
  standalone: true,
  imports: [
    ListCardComponent,
    RouterLink,
    FaIconComponent
  ],
  template:`
    <div style="display: flex; flex-direction: row">
    @for(list of listCard(); track list.id) {

    <app-list-card [title]="list.name"></app-list-card>

  }</div>` ,
  styleUrl: './board.component.css'
})
export class BoardComponent implements OnInit{

  public listCard: WritableSignal<ListCard[]> = signal<ListCard[]>([]);
  public boardService : BoardService = inject(BoardService);
  private route : ActivatedRoute = inject(ActivatedRoute);
  private boardId : number = 0;

  ngOnInit() {

    this.boardId = this.route.snapshot.params['id'];
    this.getListCards(this.boardId)

  }

  getListCards(boardId: number): void {
    this.boardService.getListCards(boardId).subscribe({
      next: (data: ListCard[]) => {
        this.listCard.set(data);
      }
    });
  }


}
