import {Component, OnInit, inject, signal, Input, WritableSignal} from '@angular/core';
import { Card } from '../../../dao/card';
import { ListCardService } from '../../../service/list-cards/list-card.service';
import {CardComponent} from '../card/card.component';

import {HorizontalDragDropExampleComponent} from '../horizontal/horizontal.component';

import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import {MatDatepicker, MatDatepickerInput, MatDatepickerToggle} from '@angular/material/datepicker';
import {MatHint, MatInput, MatSuffix} from '@angular/material/input';
import {provideNativeDateAdapter} from '@angular/material/core';

@Component({
  selector: 'app-list-card',
  standalone: true,
  providers: [provideNativeDateAdapter()],
  imports: [
    CardComponent,
    HorizontalDragDropExampleComponent,
    FormsModule,

    ReactiveFormsModule,
    MatDatepickerInput,
    MatInput,
    MatHint,
    MatDatepickerToggle,
    MatDatepicker,
    MatSuffix

  ],
  templateUrl:'./list-card.component.html',
  styleUrl: './list-card.component.css'
})
export class ListCardComponent implements OnInit {
  @Input() title = '';
  @Input() listCardId = 0;
  myForm : FormGroup;
  public cards : WritableSignal<Card[]> = signal<Card[]>([]);
  private formBuilder : FormBuilder = inject(FormBuilder)
  public listCardService = inject(ListCardService);
  isClicked: boolean = false;

  constructor() {
    this.myForm = this.formBuilder.group({
      name: ['', Validators.required],

    });
  }
  ngOnInit(): void {
    this.getListCard(this.listCardId);

  }
  onClickEvent(event: MouseEvent): void {
    event.stopPropagation(); // Empêche le clic de se propager
  }
  public getListCard(listCardId : number) : void{
    this.listCardService.getCards(listCardId).subscribe({
      next: (data: Card[]) => {
        this.cards.set(data);
      }
    })
  }

  addCard() {
    const card = this.myForm.value
    this.listCardService.createCard(this.listCardId,card).subscribe({
      next: (response: Card ) => {
        console.log(response);
        this.cards.update((currentCard) => [...currentCard,card]);
        this.isClicked = false;
      }
    })
  }
}
