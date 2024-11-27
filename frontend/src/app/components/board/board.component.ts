import {Component, computed, inject, OnInit, signal, WritableSignal} from '@angular/core';
import { BoardService } from '../../../service/boards/board.service';
import {ListCardComponent} from '../list-card/list-card.component';
import {ActivatedRoute, RouterLink} from '@angular/router';
import {FaIconComponent} from '@fortawesome/angular-fontawesome';
import {ListCard} from '../../../dao/list-card';
import {MainService} from '../../../service/main/main.service';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {ListCardService} from '../../../service/list-cards/list-card.service';
import {HorizontalDragDropExampleComponent} from '../horizontal/horizontal.component';
import {CdkDragDrop, CdkDrag, CdkDropList, moveItemInArray, CdkDragPlaceholder} from '@angular/cdk/drag-drop';

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
    CdkDrag,
    CdkDragPlaceholder
  ],
  templateUrl:'./board.component.html' ,
  styleUrl: './board.component.scss'
})
export class BoardComponent implements OnInit{
  myForm : FormGroup;
  public listCard: WritableSignal<ListCard[]> = signal<ListCard[]>([]);
  sortedListCard = computed(() =>
    this.listCard().slice().sort((a, b) => a.orderIndex - b.orderIndex)
  );
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
         this.listCard.update((currentList) => [...currentList, data])

       }
     })
   }
  }
  ngOnInit() {

    this.boardId = this.route.snapshot.params['id'];
    this.getListCards(this.boardId)

  }

  getListCards(boardId: number): void {
    this.boardService.getListCards(boardId).subscribe({
      next: (data: ListCard[]) => {
        console.log(data)
        this.listCard.set(data);
      }
    });

  }
  drop(event : CdkDragDrop<ListCard[]>) {
      moveItemInArray(this.sortedListCard(),event.previousIndex,event.currentIndex);

      this.sortedListCard().forEach((item, index) => {
        item.orderIndex = index + 1;
      });

      // Appelez le backend pour sauvegarder les modifications d'ordre
      this.boardService.updateListCard(this.boardId, this.sortedListCard()).subscribe({
        next: (response) => {
          console.log("Ordre mis à jour avec succès", response);
        },
        error: (err) => {
          console.error("Erreur lors de la mise à jour de l'ordre", err);
          // Vous pouvez également restaurer l'état précédent si nécessaire en cas d'erreur
        }
      });
    }


  closeButton() {
    this.isClicked = false;
  }
}
