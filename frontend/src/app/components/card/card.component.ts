import {
  ChangeDetectionStrategy, ChangeDetectorRef,
  Component,
  computed, effect,
  inject,
  Input,
  OnInit, Signal,
  signal,
  WritableSignal
} from '@angular/core';

import {TaskBoxComponent} from '../task-box/task-box.component';
import {

  MatDatepickerInput,
  MatDatepickerInputEvent,
  MatDatepickerToggle
} from '@angular/material/datepicker';
import {MatIconModule} from '@angular/material/icon';
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import {MAT_DATE_LOCALE} from '@angular/material/core';
import {MatButton, MatFabButton, MatIconButton, MatMiniFabButton} from '@angular/material/button';
import {AngularEditorConfig, AngularEditorModule} from '@kolkov/angular-editor';
import {MatTooltip} from '@angular/material/tooltip';
import {CheckList} from '../../../dao/check-list';
import {MatCheckbox} from '@angular/material/checkbox';
import {CardService} from '../../../service/cards/card.service';
import {Card} from '../../../dao/card';
import {MatProgressBar} from '@angular/material/progress-bar';
import {formatNumber, NgForOf} from '@angular/common';
//import {MatTimepicker, MatTimepickerInput, MatTimepickerToggle} from '@dhutaryan/ngx-mat-timepicker';
import {MatInputModule} from '@angular/material/input';
import {MatFormFieldModule} from '@angular/material/form-field';
import {provideNativeDateAdapter} from '@angular/material/core';
import {MatDatepickerModule} from '@angular/material/datepicker';
import {MatTimepickerModule} from '@dhutaryan/ngx-mat-timepicker';




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
    FormsModule,
    MatButton,
    MatIconModule,
    AngularEditorModule,
    MatMiniFabButton,
    MatTooltip,
    MatCheckbox,
    MatProgressBar,
    NgForOf,
    MatFabButton,
    MatFormFieldModule,
    MatInputModule,
    MatTimepickerModule,
    MatDatepickerModule,
    FormsModule,
    MatIconButton,

  ],
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl:'./card.component.html',
  styleUrl: './card.component.scss'
})
export class CardComponent implements OnInit {
  value: Date = new Date();
  // @ts-ignore
  @Input() card: Card = {
    id:0,
    name: "",
    description: "",
    deadline: new Date(),
    labels: [],
    checkList: [],

  };
  private formBuilder : FormBuilder = inject(FormBuilder)
  protected showOptions: boolean[] =[]
  checkListSignal: WritableSignal<CheckList[]> = signal<CheckList[]>([]);
protected now : Date = new Date()
  protected selectedTime = "";
  protected readonly formatNumber = formatNumber;
  protected readonly Number = Number;
  myForm: FormGroup;
  //protected time = this.selectedTime.split(":").map(Number);
protected  checkList : CheckList[] | undefined = [];
  private cardService: CardService = inject(CardService);
  protected description: string | undefined = "";
  protected isHover = false;
  protected labelColor: string[] = [];
  protected isClicked = false;
  isTaskCompleted = signal(false);
  protected descriptionConfig = "";

  isModalOpen = false;
  editorConfig: AngularEditorConfig = {
    editable: true,
    spellcheck: true,
    height: 'auto',
    minHeight: '0',
    maxHeight: 'auto',
    width: 'auto',
    minWidth: '0',
    translate: 'yes',
    enableToolbar: true,
    showToolbar: true,
    placeholder: 'Enter text here...',
    defaultParagraphSeparator: '',
    defaultFontName: '',
    defaultFontSize: '',
    fonts: [
      {class: 'arial', name: 'Arial'},
      {class: 'times-new-roman', name: 'Times New Roman'},
      {class: 'calibri', name: 'Calibri'},
      {class: 'comic-sans-ms', name: 'Comic Sans MS'}
    ],
    customClasses: [
      {
        name: 'quote',
        class: 'quote',
      },
      {
        name: 'redText',
        class: 'redText'
      },
      {
        name: 'titleText',
        class: 'titleText',
        tag: 'h1',
      },
    ],
    uploadUrl: 'v1/image',
    uploadWithCredentials: false,
    sanitize: true,
    toolbarPosition: 'top',
    toolbarHiddenButtons: [
      ['bold', 'italic'],
      ['fontSize']
    ]
  };

  progress: number = 0;
  private cd: ChangeDetectorRef = inject(ChangeDetectorRef);
 constructor() {
   this.myForm = this.formBuilder.group({
     name: ['', Validators.required],

   });
 }
  // Calcul du nombre de tâches terminées
  protected isClickedList: boolean  = false;
  get completedTasksCount(): number {
    return this.checkListSignal().filter(task => task.completed).length;
  }

 completedTasks = computed(() => {

    return this.checkListSignal().filter((item => !this.hideCompleted() || !item.completed)) ;

  })

  // Total des tâches
  get totalTasksCount(): number {
    return this.checkListSignal().length;
  }
  events = signal<string[]>([]);
  hideCompleted: WritableSignal<boolean> = signal<boolean>(false);

  addEvent(type: string, event: MatDatepickerInputEvent<Date>) {
    this.events.update(events => [...events, `${type}: ${event.value}`]);
    console.log(event.value?.toJSON())
    /*if(event.value){
      this.deadline = new Date(event.value?.toJSON());
    }*/

  }
  onTimeChange() {
    const [hours, minutes] = this.selectedTime.split(":").map(Number);

    this.card.deadline = new Date(this.card.deadline) // Définit l'heure
    this.card.deadline.setHours(hours, minutes, 0, 0);
    console.log(this.card.deadline);

  }
  updateProgress(completed: boolean, index: number): void {

    if(this.completedTasksCount == 0 || this.totalTasksCount == 0)
      this.progress = 0;
    else {
      this.checkListSignal.update(list => {
        list[index].completed = completed;
        return list;
      })

      this.progress = Math.floor((this.completedTasksCount / this.totalTasksCount) * 100);
    }

  }


  ngOnInit(): void {
    this.labelColor = this.card.labels.map(label => label.color)
    this.checkList = this.card.checkList;
    this.checkListSignal.set(this.checkList);
   // this.description = this.card.description;
    this.showOptions = new Array(this.checkListSignal().length).fill(false)
    this.card.deadline = new Date(this.card.deadline)

    const hours = (this.card.deadline.getHours() + 1).toString();
    const minutes  = this.card.deadline.getMinutes() >= 0 && this.card.deadline.getMinutes() <= 9 ? "0" +
      this.card.deadline.getMinutes() : this.card.deadline.getMinutes();
    this.selectedTime = hours + ":" + minutes
    this.progress = this.completedTasksCount === 0 ? 0 : Math.floor((this.completedTasksCount / this.totalTasksCount) * 100);
     console.log( this.selectedTime)
  }

  hover() {
    this.isHover = true;
  }
  hoverList(idx: number) {
    this.showOptions[idx] = true;
  }



  openModal() {
    this.isModalOpen = true;
  }

  handleConfirm(card: Card) {
    console.log('Confirmé !');


   // card.description = this.description;
    this.cardService.updateCard(this.card.id, card).subscribe({
      next: (response) => {
        console.log('Success :' + response);
      },
      error: (err) => {
        console.error('Error : ' + err);
      },
    });

    this.isModalOpen = false;
  }

  handleCancel() {
    this.isModalOpen = false;
  }


  setHideSelected() {
      this.hideCompleted.set(!this.hideCompleted())
  }

  addItemList() {
    this.isClickedList = true;
  }
  addItemListConfirm() {

    const checkList: CheckList = {

      name: this.myForm.value.name,
      completed: false
    };

    // Créez une nouvelle référence pour la liste
    const updatedCheckList = [...this.card.checkList, checkList];

    // Mettez à jour l'objet card avec la nouvelle liste
    const updatedCard = { ...this.card, checkList: updatedCheckList };
    this.cardService.updateCard(this.card.id,this.card).subscribe({
      next: (response) => {
        console.log('Success :' + response);
        this.checkListSignal.set(updatedCheckList);
        this.cd.markForCheck();
      },
      error: (err) => {
        console.error('Error : ' + err);
      }

    });

    this.myForm.patchValue({
      name:""
    })
    this.isClickedList = false;
  }

  hoverListOut(i: number) {
    this.showOptions[i] = false;
  }
}
