import {
  ChangeDetectionStrategy, ChangeDetectorRef,
  Component,
  computed, effect,
  inject,
  Input, model,
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
import {DatePipe, formatNumber, NgForOf} from '@angular/common';
//import {MatTimepicker, MatTimepickerInput, MatTimepickerToggle} from '@dhutaryan/ngx-mat-timepicker';
import {MatInputModule} from '@angular/material/input';
import {MatFormFieldModule} from '@angular/material/form-field';
import {provideNativeDateAdapter} from '@angular/material/core';
import {MatDatepickerModule} from '@angular/material/datepicker';
import {MatTimepickerModule} from '@dhutaryan/ngx-mat-timepicker';
import {MatCard} from '@angular/material/card';
import {MatChip} from '@angular/material/chips';




@Component({
  selector: 'app-card',
  standalone: true,
  providers: [provideNativeDateAdapter(),DatePipe, {provide: MAT_DATE_LOCALE, useValue: 'fr'},
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
    MatCard,
    MatChip,

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
    hours: 0,
    minutes: 0,
    labels: [],
    checkList: [],

  };
  protected date = model<Date>(new Date());
  private formBuilder : FormBuilder = inject(FormBuilder)
  protected showOptions: WritableSignal<boolean[]> = signal([])
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
  protected datePipe: DatePipe = inject(DatePipe)
  progress: number = 0;
  private cd: ChangeDetectorRef = inject(ChangeDetectorRef);
  protected updateOptions: boolean[] = [];
  protected count: number = 0;
  protected openDate: boolean = false;
  protected selectedDate: string | null = "";
  dateChecked: boolean = false;
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

    this.card.hours = hours // Définit l'heure
    this.card.minutes = minutes;

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
    this.showOptions.set(Array(this.checkListSignal().length).fill(false));
    this.updateOptions = new Array(this.checkListSignal().length).fill(false);
    this.dateChecked = this.card.isCompleted;
    this.card.deadline = new Date(this.card.deadline)
    this.count = 0;
    const hours = this.card.hours === null ? 0 : this.card.hours;
    const minutes  = this.card.minutes === null? 0 : this.card.minutes;
    this.date.set(new Date(this.card.deadline.toISOString().split('T')[0]));
    this.selectedDate = this.card.deadline.toISOString().split('T')[0]
    this.selectedTime = hours + ":" + minutes
    this.progress = this.completedTasksCount === 0 ? 0 : Math.floor((this.completedTasksCount / this.totalTasksCount) * 100);
     console.log(this.selectedTime)
  }


  onDateChange(event: any): void {
    // Formater la date au format DD/MM/YYYY
    if(this.selectedDate !== null){
      this.selectedDate = this.datePipe.transform(this.date(), 'dd/MM/yyyy','fr')
    }


  }
  isDateValid(): boolean {
    // Vérifie si selectedDate est différent de null et si c'est une date valide
    if (this.selectedDate !== null) {
      const selectedDateObj = new Date(this.selectedDate) // Convertit selectedDate en objet Date

      // Vérifie que selectedDate est une date valide avant la comparaison
      if (!isNaN(selectedDateObj.getTime())) {
        return  this.now > selectedDateObj;
      }
    }
    return false;
  }
  hover() {
    this.isHover = true;
  }
  hoverList(idx: number) {
    this.showOptions.update(list => {
      list[idx] = true;
      return list
    });
  }



  openModal() {
    this.isModalOpen = true;
  }

  handleConfirm(card: Card) {

    this.card.deadline = this.date();
  this.card.isCompleted = this.dateChecked;
    console.log(this.date());
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
    this.showOptions.update(list => {
      list[i] = false;
      return list;
    })
  }

  updateList(i: number) {

    if(!this.updateOptions[i]) {
      this.updateOptions[i] = true;
      this.myForm = this.formBuilder.group({
        name: [this.checkListSignal()[i].name, Validators.required],

      });

      // Créez une nouvelle référence pour la liste

      this.cardService.updateCheckList(this.card.id,this.checkListSignal()[i]).subscribe({
        next: (response) => {
          console.log('Success :' + response);

          this.cd.markForCheck();
        },
        error: (err) => {
          console.error('Error : ' + err);
        }

      });
    }

  }

  unShowOptions(i: number) {
    console.log()
    this.checkListSignal.update(list => {
      list[i].name = this.myForm.value.name;
      return list;
    })
    this.updateOptions[i] = false
  }

  protected readonly length = length;

  openDateModal() {
    this.openDate = true;
  }


  updateDateTime() {
    if(this.selectedDate !== undefined && this.date() !== null){
      this.selectedDate = this.date().toDateString()
      this.openDate = false
    }

  }
}
