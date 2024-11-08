import {Component, EventEmitter, inject, OnInit, Output, signal, WritableSignal} from '@angular/core';
import { BoardService } from '../../service/boards/board.service';
import {ListCardComponent} from '../list-card/list-card.component';
import {ActivatedRoute, RouterLink} from '@angular/router';
import {FaIconComponent} from '@fortawesome/angular-fontawesome';
import {ListCard} from '../../dao/list-card';
import {MainService} from '../../service/main/main.service';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {ListCardService} from '../../service/list-cards/list-card.service';
import {HorizontalDragDropExampleComponent} from '../horizontal/horizontal.component';
import {CdkDragDrop, CdkDrag, CdkDropList, moveItemInArray} from '@angular/cdk/drag-drop';

@Component({
  selector: 'app-board',
  standalone: true,
  imports: [
    ListCardComponent,
    RouterLink,
    FaIconComponent,
    ReactiveFormsModule,
    HorizontalDragDropExampleComponent,
    CdkDropList,
    CdkDrag
  ],
  templateUrl:'./board.component.html' ,
  styleUrl: './board.component.css'
})
export class BoardComponent implements OnInit{
  myForm : FormGroup;
  public listCard: WritableSignal<ListCard[]> = signal<ListCard[]>([]);
  public listCardService : ListCardService = inject(ListCardService);
  public boardService : BoardService = inject(BoardService);
  mainService : MainService = inject(MainService)
  private route : ActivatedRoute = inject(ActivatedRoute);
  private boardId : number = 0;
  protected isClicked: boolean = false;

  formBuilder : FormBuilder = inject(FormBuilder);
  constructor() {
    this.myForm = this.formBuilder.group({
      name: ['', Validators.required],

    });
  }
  buttonClicked() {
    this.isClicked = true;
  }
  addList() {
   const listCard: ListCard =  this.myForm.value;
   if(listCard.name !== ""){
     this.boardService.createListCard(this.boardId,listCard).subscribe({
       next: (data: ListCard) =>{
         this.isClicked = false;
         this.listCard().push(data);
       }
     })
   }
  }
  ngOnInit() {

    this.boardId = this.route.snapshot.params['id'];
    this.getListCards(this.boardId)

    console.log(this.mainService.getListBoards())

  }

  getListCards(boardId: number): void {
    this.boardService.getListCards(boardId).subscribe({
      next: (data: ListCard[]) => {
        this.listCard.set(data);
      }
    });
  }
  drop(event : CdkDragDrop<ListCard[]>) {
      moveItemInArray(this.listCard(),event.previousIndex,event.currentIndex);
  }



}
