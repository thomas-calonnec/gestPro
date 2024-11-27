import {ChangeDetectionStrategy, Component, Input, OnInit, signal} from '@angular/core';

import {TaskBoxComponent} from '../task-box/task-box.component';
import { MatFormFieldModule} from '@angular/material/form-field';
import {

  MatDatepickerInput,
  MatDatepickerInputEvent, MatDatepickerModule,
  MatDatepickerToggle
} from '@angular/material/datepicker';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {MatInput, MatInputModule} from '@angular/material/input';
import {MAT_DATE_LOCALE, provideNativeDateAdapter} from '@angular/material/core';


@Component({
  selector: 'app-card',
  standalone: true,
  providers: [provideNativeDateAdapter(), {provide: MAT_DATE_LOCALE, useValue: 'fr'},
  ],
  imports: [
    TaskBoxComponent,
    MatFormFieldModule,
    MatDatepickerToggle,
    MatDatepickerModule,
    ReactiveFormsModule,
    MatDatepickerInput,
    MatInputModule,
    FormsModule

  ],
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl:'./card.component.html',
  styleUrl: './card.component.scss'
})
export class CardComponent implements OnInit {
@Input() taskName = "";
@Input() deadline : Date = new Date()                         ;
protected isHover = false;
protected isClicked = false;
protected selectedTime = "";
  isModalOpen = false;
  events = signal<string[]>([]);

  addEvent(type: string, event: MatDatepickerInputEvent<Date>) {
    this.events.update(events => [...events, `${type}: ${event.value}`]);
    console.log(event.value?.toJSON())
    /*if(event.value){
      this.deadline = new Date(event.value?.toJSON());
    }*/

  }

  ngOnInit(): void {
  }
  hover() {
    this.isHover = true;
  }


  openModal() {
    this.isModalOpen = true;
  }

  handleConfirm() {
    console.log('Confirmé !');
    this.isModalOpen = false;
  }

  handleCancel() {
    this.isModalOpen = false;
  }


}
