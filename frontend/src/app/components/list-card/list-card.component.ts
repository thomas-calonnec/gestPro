import {
  Component,
  OnInit,
  inject,
  signal,
  Input,
  WritableSignal,
  Output,
  EventEmitter,
  HostListener, ElementRef
} from '@angular/core';
import { Card } from '@/models/card';
import { ListCardService } from '@/services/list-cards/list-card.service';
import {CardComponent} from '../card/card.component';
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import {provideNativeDateAdapter} from '@angular/material/core';
import {ListCard} from '@/models/list-card';
import {BoardService} from '@/services/boards/board.service';

@Component({
  selector: 'app-list-card',
  standalone: true,
  providers: [provideNativeDateAdapter()],
  imports: [
    CardComponent,
    FormsModule,
    ReactiveFormsModule,
  ],
  templateUrl:'./list-card.component.html',
  styleUrl: './list-card.component.css'
})
export class ListCardComponent implements OnInit {

  @Output() listCardEmit  = new EventEmitter<ListCard>()
  myForm : FormGroup;
  protected card : Card = {
    id:0,
    name: "",
    description: "",
    deadline: new Date(),
    hours: 0,
    minutes: 0,
    labels: [],
    checkList: [],
    isCompleted: false,
    isDateActivated: false
  }
  @Input() listCard : ListCard = {
    id: 0,
    name: '',
    orderIndex: 0,
    isArchived: false
  }
  public cards : WritableSignal<Card[]> = signal<Card[]>([]);
  private formBuilder : FormBuilder = inject(FormBuilder)
  public listCardService = inject(ListCardService);
  public boardService = inject(BoardService);
  isClicked: boolean = false;
  modifyTitle: boolean = false;
  // Écoute globale des clics sur le document
  @HostListener('document:click', ['$event'])
  handleOutsideClick(event: MouseEvent) {
    const clickedInside = this.elementRef.nativeElement.contains(event.target);
    if (!clickedInside) {
      this.disableEdit();
    }
  }
  disableEdit() {
    this.modifyTitle = false;
  }
  constructor(private elementRef: ElementRef) {
    this.myForm = this.formBuilder.group({
      name: ['', Validators.required],

    });
  }
  ngOnInit(): void {
    this.getListCard(this.listCard.id);
  }

  public getListCard(listCardId : number) : void{
    this.listCardService.getCards(listCardId).subscribe({
      next: (data: Card[]) => {
        this.cards.set(data);
      }
    })
  }

  addCard() {
    this.card.name = this.myForm.value.name;
    this.listCardService.createCard(this.listCard.id,this.card).subscribe({
      next: (data: Card ) => {
        this.isClicked = false;
        this.cards.update((currentCard) => [...currentCard, data]);

      }
    })
  }


  modifyTitleConfirm() {
   this.modifyTitle = false;
    this.listCardEmit.emit(this.listCard);

  }

  archiveListCard() {
    this.listCard.isArchived = true;
    this.listCardEmit.emit(this.listCard);
  }
}
