import {Component, computed, inject, Input, OnInit, signal, WritableSignal} from '@angular/core';
import { BoardService } from '@services/boards/board.service';
import {ActivatedRoute, RouterLink} from '@angular/router';
import {ListCard} from '@models/list-card';
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import {
  CdkDrag,
  CdkDragDrop, CdkDropList,
  moveItemInArray
} from '@angular/cdk/drag-drop';
import {Board} from '@models/board';
import {MainService} from '@services/main/main.service';
import {ListCardComponent} from '@components/list-card/list-card.component';
import {MatButton} from '@angular/material/button';

@Component({
    selector: 'app-board',
  imports: [
    ReactiveFormsModule,
    RouterLink,
    FormsModule,
    CdkDropList,
    ListCardComponent,
    CdkDrag,
    MatButton,
  ],
    templateUrl: './board.component.html',
    styleUrl: './board.component.scss'
})
export class BoardComponent implements OnInit{
  myForm : FormGroup;
  public listCard: WritableSignal<ListCard[]> = signal<ListCard[]>([]);
  sortedListCard = computed(() =>
    this.listCard().slice().sort((a, b) => a.orderIndex - b.orderIndex).filter((list) => !list.isArchived)
  );
  mainService: MainService = inject(MainService);

  public boardService : BoardService = inject(BoardService);
  private route : ActivatedRoute = inject(ActivatedRoute);
  private boardId : number = 0;
  protected isClicked: boolean = false;

  formBuilder : FormBuilder = inject(FormBuilder);
  boardName: string = "";

  constructor() {
    this.myForm = this.formBuilder.group({
      name: ['', Validators.required],

    });
  }
  buttonClicked() {
    this.isClicked = true;
  }

  addList() {
    const listCard: ListCard = {

      name:  this.myForm.value.name.toLowerCase(),
      orderIndex: -1,
      isArchived: false
    }


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
    this.getListCards(this.boardId);

    this.boardService.getBoardById(this.boardId).subscribe({
      next: board => {
        this.boardName = board.name
      }
    })

  }
  autoResize(textarea: HTMLTextAreaElement): void {
    textarea.style.height = 'auto';
    textarea.style.height = textarea.scrollHeight + 'px';
  }
  getListCards(boardId: number): void {
    this.boardService.getListCards(boardId).subscribe({
      next: (data: ListCard[]) => {
        this.listCard.set(data);
      }
    });

  }

  // }
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

  updateListCard(updateListCard: ListCard) {

    this.listCard().map(list => {

      if (list.id === updateListCard.id) {
        // Mettre à jour les champs nécessaires

        list.isArchived = updateListCard.isArchived

        return {...list, ...updateListCard};
      }
      return list; // Les autres cartes restent inchangées
    })


    this.boardService.updateListCard(this.boardId, this.listCard()).subscribe({
      next: (response) => {
        console.log("Ordre mis à jour avec succès", response);
      },
      error: (err) => {
        console.error("Erreur lors de la mise à jour de l'ordre", err);
        // Vous pouvez également restaurer l'état précédent si nécessaire en cas d'erreur
      }
    });
    this.listCard.update(list => {
      return list.filter(l => !l.isArchived);
    })
  }


    protected readonly localStorage = localStorage;
    protected readonly Number = Number;
  @Input() board!: Board;
}
