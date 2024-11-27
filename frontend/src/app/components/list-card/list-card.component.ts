import {Component, OnInit, inject, signal, Input, WritableSignal} from '@angular/core';
import { Card } from '../../../dao/card';
import { ListCardService } from '../../../service/list-cards/list-card.service';
import {CardComponent} from '../card/card.component';

import {HorizontalDragDropExampleComponent} from '../horizontal/horizontal.component';
import {FormGroup, FormsModule, ReactiveFormsModule, Validators, FormBuilder} from '@angular/forms';

@Component({
  selector: 'app-list-card',
  standalone: true,
  imports: [
    CardComponent,
    HorizontalDragDropExampleComponent,
    FormsModule,
    ReactiveFormsModule
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

        @if(!isClicked){
          <div class="card-footer" >

            <a (click)="isClicked = true" style="display: contents"><div style="margin-right: 10px;"> <i class="fa-solid fa-circle-plus"></i></div>  Add a card</a>
          </div>
        } @else {
          <div class="card-footer">
            <form  [formGroup]="myForm" (ngSubmit)="addCard()" style="background-color: #2A3E52; border-radius: 12px ;">
              <div class="myInput">
                <input type="text" class="form-control h-25 opacity-75 inputListCard" formControlName="name" maxlength="512" required="required" aria-label="Small"  placeholder="Enter list name" aria-describedby="inputGroup-sizing-sm">
              </div>
              <button type="submit" class="btn btn-sm" style="margin: 10px; background-color: #579DFF; text-decoration: none;"> Add card </button>
              <button type="submit"  class="btn btn-light btn-sm" (click)="this.isClicked = false" ><i class="fa-solid fa-times" ></i></button>
            </form>


          </div>
        }


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
