import {Component, computed, inject,  OnInit, signal, WritableSignal} from '@angular/core';
import { BoardService } from '@services/boards/board.service';
import {ActivatedRoute, RouterLink} from '@angular/router';
import {ListCard} from '@models/list-card';
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import {
  CdkDrag, CdkDragDrop,
  CdkDropList, moveItemInArray,
} from '@angular/cdk/drag-drop';
import {MainService} from '@services/main/main.service';
import {MatButton} from '@angular/material/button';
import {MatFormFieldModule} from '@angular/material/form-field';
import {ListCardComponent} from '@components/list-card/list-card.component';
import {WorkspaceService} from '@services/workspaces/workspace.service';
import {ListCardService} from '@services/list-cards/list-card.service';

@Component({
    selector: 'app-board',
  imports: [
    ReactiveFormsModule,
    RouterLink,
    FormsModule,
    MatButton,
    MatFormFieldModule,
    CdkDropList,
    ListCardComponent,
    CdkDrag,
  ],
    templateUrl: './board.component.html',
    styleUrl: './board.component.scss'
})
export class BoardComponent implements OnInit{

  mainService: MainService = inject(MainService);

  public listCard: WritableSignal<ListCard[]> = signal<ListCard[]>([]);
  sortedListCard = computed(() =>
    this.listCard().slice().sort((a, b) => a.orderIndex - b.orderIndex).filter((list) => !list.isArchived)
  );

  public boardService : BoardService = inject(BoardService);
  protected workspaceService: WorkspaceService = inject(WorkspaceService);
  protected listCardService: ListCardService = inject(ListCardService);
  private boardId : number | undefined;
  private route : ActivatedRoute = inject(ActivatedRoute);
  protected isClicked: boolean = false;
  workspaceName : string | undefined;
  workspaceId : number | undefined;
  boardName: string = "";
  myForm : FormGroup;
  formBuilder : FormBuilder = inject(FormBuilder);

  constructor() {
    this.myForm = this.formBuilder.group({
      name: ['', Validators.required],

    });
  }


  ngOnInit() {
    this.boardId = this.route.snapshot.params['id'];
    this.workspaceId = Number(localStorage.getItem("workspaceId"));

    if(this.boardId) {
      this.boardService.getBoardById(this.boardId).subscribe({
        next: board => {
          this.boardName = board.name
        }
      });

      this.workspaceService.getWorkspaceByBoardId(this.boardId).subscribe({
        next: workspace => {
          this.workspaceId = workspace.id;
          this.workspaceName = workspace.name;
        }
      });
      this.getListCards(this.boardId);

    }

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
      this.listCardService.createListCard(this.boardId!,listCard).subscribe({
        next: (data: ListCard) =>{
          this.isClicked = false;
          this.listCard.update((currentList) => [...currentList, data])

        }
      })
    }
  }

  autoResize(textarea: HTMLTextAreaElement): void {
    textarea.style.height = 'auto';
    textarea.style.height = textarea.scrollHeight + 'px';
  }

  drop(event : CdkDragDrop<ListCard[]>) {
    moveItemInArray(this.sortedListCard(),event.previousIndex,event.currentIndex);

    this.sortedListCard().forEach((item, index) => {
      item.orderIndex = index + 1;
    });

    // Appelez le backend pour sauvegarder les modifications d'ordre
    this.listCardService.updateListCard(this.boardId!, this.sortedListCard()).subscribe({
      next: (response) => {
        console.log("Ordre mis à jour avec succès", response);
      },
      error: (err) => {
        console.error("Erreur lors de la mise à jour de l'ordre", err);
        // Vous pouvez également restaurer l'état précédent si nécessaire en cas d'erreur
      }
    });
  }

  getListCards(boardId: number): void {
    this.listCardService.getListCards(boardId).subscribe({
      next: (data: ListCard[]) => {
        this.listCard.set(data);
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


    this.listCardService.updateListCard(this.boardId!, this.listCard()).subscribe({
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

  protected readonly Number = Number;
  protected readonly localStorage = localStorage;

}
