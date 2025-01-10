import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  computed,
  inject,
  Input,
  model,
  OnInit,
  signal,
  WritableSignal
} from '@angular/core';

import {TaskBoxComponent} from '../task-box/task-box.component';

import {MatIconModule} from '@angular/material/icon';
import {FormBuilder, FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import {MAT_DATE_LOCALE, provideNativeDateAdapter} from '@angular/material/core';
import {MatButton, MatIconButton, MatMiniFabButton} from '@angular/material/button';
import {AngularEditorModule} from '@kolkov/angular-editor';
import {MatTooltip} from '@angular/material/tooltip';
import {CheckList} from '@models/check-list';
import {MatCheckbox} from '@angular/material/checkbox';
import {CardService} from '@services/cards/card.service';
import {Card} from '@models/card';
import {MatProgressBar} from '@angular/material/progress-bar';
import {DatePipe} from '@angular/common';
import {MatInputModule} from '@angular/material/input';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatDatepickerModule} from '@angular/material/datepicker';
import {MatTimepickerModule} from '@dhutaryan/ngx-mat-timepicker';
import {MatCard, MatCardHeader} from '@angular/material/card';
import {LabelService} from '@services/labels/label.service';
import {Label} from '@models/label';

@Component({
  selector: 'app-card',
  standalone: true,
  providers: [provideNativeDateAdapter(),DatePipe, {provide: MAT_DATE_LOCALE, useValue: 'fr'},
  ],
  imports: [
    TaskBoxComponent,
    MatFormFieldModule,
    MatDatepickerModule,
    ReactiveFormsModule,
    MatInputModule,
    FormsModule,
    MatButton,
    MatIconModule,
    AngularEditorModule,
    MatMiniFabButton,
    MatTooltip,
    MatCheckbox,
    MatProgressBar,
    MatFormFieldModule,
    MatInputModule,
    MatTimepickerModule,
    MatDatepickerModule,
    FormsModule,
    MatIconButton,
    MatCard,
    MatCardHeader,


  ],
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl:'./card.component.html',
  styleUrl: './card.component.scss'
})
export class CardComponent implements OnInit {
  value: Date = new Date();

  @Input() card: Card = {
    id: 0,
    name: "",
    description: "",
    deadline: new Date(),
    hours: 0,
    minutes: 0,
    labels: [],
    checkList: [],
    isCompleted: false,
    isHovered: false,
    isDateActivated: false

  };
  errorMessage = signal('');
  readonly time = new FormControl('', [Validators.required]);
  protected date = model<Date>(new Date());
  private formBuilder : FormBuilder = inject(FormBuilder)
  protected showOptions: WritableSignal<boolean[]> = signal([])
  checkListSignal: WritableSignal<CheckList[]> = signal<CheckList[]>([]);
  isSoon = signal(false);
  protected now : Date = new Date()
  protected selectedTime = ""
  myForm: FormGroup;
  protected  checkList : CheckList[] | undefined = [];
  private cardService: CardService = inject(CardService);
  protected labels: Label[] = [];
  protected isClicked = false;
  isModalOpen = false;
  protected datePipe: DatePipe = inject(DatePipe)
  progress: number = 0;
  private labelService = inject(LabelService);
  private cd: ChangeDetectorRef = inject(ChangeDetectorRef);
  protected updateOptions: boolean[] = [];
  protected count: number = 0;
  protected openDate: boolean = false;
  protected selectedDate: string | null = "";
  dateChecked: boolean = false;
  openLabel: boolean = false;
  protected signalTime = signal("");


  constructor() {
   this.myForm = this.formBuilder.group({
     name: ['', Validators.required],

   });
   this.formDate = this.formBuilder.group({})
 }
  // Calcul du nombre de tâches terminées
  protected isClickedList: boolean  = false;
  get completedTasksCount(): number {
    return this.checkListSignal().filter(task => task.completed).length;
  }

  onMouseEnter(task: Card): void {
    task.isHovered = true;
  }

  onMouseLeave(task: Card): void {
    task.isHovered = false;
  }

  completedTasks = computed(() => {

    return this.checkListSignal().filter((item => !this.hideCompleted() || !item.completed)) ;

  })
   formatDate(date: Date): string {
    return new Intl.DateTimeFormat('en-US', { day: '2-digit', month: 'short' }).format(date);
  }

  // Total des tâches
  get totalTasksCount(): number {
    return this.checkListSignal().length;
  }

  hideCompleted: WritableSignal<boolean> = signal<boolean>(false);
  onTimeChange() {
    const timeValue = this.formDate.get("time");
    if(timeValue != null) {
      const [hours, minutes] = timeValue.value.split(":").map(Number);

      this.card.hours = hours // Définit l'heure
      this.card.minutes = minutes;
      this.signalTime.set(timeValue.value)

    }


  }
  onCheckedChange(dateChecked: boolean){
    this.dateChecked = dateChecked;
    this.isSoon.set(this.isDueSoon());

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
    if(this.card !== null && this.card.checkList !== undefined){

      const now = new Date();
      this.checkList = this.card.checkList.length > 0 ? this.card.checkList : [];
      this.checkListSignal.set(this.checkList);
      this.showOptions.set(Array(this.checkListSignal().length).fill(false));
      this.updateOptions = new Array(this.checkListSignal().length).fill(false);
      this.dateChecked = this.card.isCompleted;
      this.card.deadline = new Date(this.card.deadline)
      this.count = 0;
      this.isSoon.set(this.isDueSoon());
      const hours = this.card.hours === null ? now.getHours() : this.card.hours;
      const minutes  = this.card.minutes === null? now.getMinutes() : this.card.minutes;
      this.date.set(new Date(this.card.deadline.toISOString().split('T')[0]));
      this.selectedDate = this.datePipe.transform(this.date(), 'dd/MM/yyyy','fr') || ''
      this.selectedTime = hours + ":" + minutes
      this.signalTime.set(this.selectedTime);
      this.progress = this.completedTasksCount === 0 ? 0 : Math.floor((this.completedTasksCount / this.totalTasksCount) * 100);
      this.labelService.getLabels().subscribe({
        next: (labels) => {
          this.labels = labels;
        }
      })
    }

    this.formDate = this.formBuilder.group({
      time: [[this.selectedTime || ''], Validators.required],
      date: [[this.selectedDate || ''], Validators.required]
    })
  }

  isOverDue(): boolean {
    if (!this.date() || !this.signalTime()) {
      return false; // Si la date ou l'heure est absente, retournez false
    }

    // Formater et parser la date avec DatePipe
    const formattedDate = this.datePipe.transform(this.date(), 'yyyy-MM-dd', 'fr') || '';
    const deadlineDate = new Date(formattedDate); // Crée un objet Date pour la date limite


    // Extraire les heures et minutes depuis signalTime
    const [hours, minutes] = this.signalTime().split(':').map(Number);
    deadlineDate.setHours(hours, minutes, 0, 0); // Ajouter l'heure limite à la date

    // Comparer la date et l'heure actuelles avec la date limite
    const now = new Date();
    return now > deadlineDate; // Retourne true si maintenant dépasse la date limite
  }

  isDueSoon(): boolean {
    if (this.selectedDate !== null) {
      const selectedDateObj = this.parseDate(this.date().toLocaleDateString());
      const now = new Date(); // Obtenez la date actuelle

      // Calculer la différence en millisecondes
      const [hours, minutes] = this.signalTime().split(':').map(Number);
      // Créer un nouvel objet Date pour la limite de temps (aujourd'hui à signalTime())
      const targetDate = new Date(selectedDateObj.getFullYear(), selectedDateObj.getMonth(), selectedDateObj.getDate(), hours, minutes, 0, 0);

// Calculer la différence en millisecondes
      const diffInMs = targetDate.getTime() - now.getTime();

      // Convertir la différence en jours
      const diffInDays = diffInMs / (1000 * 60 * 60 * 24);

      // Si la date n'est pas en retard et est à moins de 1 jour
      if (!this.isOverDue() && diffInDays <= 1 && diffInDays >= 0) {
        return true;
      }
    }
    return false;
  }


  parseDate(dateStr: string): Date {
    const [day, month, year] = dateStr.split('/').map(Number);

    return new Date(`${year}/${month}/${day}`); // Les mois commencent à zéro
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

    this.card.deadline = new Date(Date.UTC(
      this.date().getFullYear(),
      this.date().getMonth(),
      this.date().getDate()+1
    ))


    this.card.isCompleted = this.dateChecked;

      this.cardService.updateCard(this.card.id, card).subscribe({
        next: () => {

        },
        error: (err) => {
          console.error('Error : ' + err);
        },
      });

      this.isModalOpen = false;


  }

  handleCancel() {
    this.myForm.patchValue({
      name:""
    })
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
    this.checkListSignal.update((currentList) => [...currentList, checkList])
  this.card.checkList = this.checkListSignal()
    this.cardService.updateCard(this.card.id, this.card).subscribe({
      next: () => {
       // this.checkListSignal.set(updatedCheckList);
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

    if (!this.updateOptions[i]) {
      this.updateOptions[i] = true;
      this.myForm = this.formBuilder.group({
        name: [this.checkListSignal()[i].name, Validators.required],
      });
    }
  }
  updateListConfirm(i: number) {
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

  unShowOptions(i: number) {
    this.checkListSignal.update(list => {
      list[i].name = this.myForm.value.name;
      return list;
    })
    this.updateListConfirm(i)
    this.updateOptions[i] = false
  }

  updateCompleteDate(card: Card) {
    this.dateChecked = !this.dateChecked;
    this.card.isCompleted = this.dateChecked;
    this.cardService.updateCard(this.card.id, card).subscribe({
      next: () => {

      },
      error: (err) => {
        console.error('Error : ' + err);
      },
    });
  }

  protected readonly length = length;
  formDate: FormGroup;
  isHoverDate: boolean = false;



  openDateModal() {
    this.openDate = true;
  }


  updateDateTime() {

    if(this.selectedDate !== undefined && this.date() !== null){
      this.selectedDate = this.date().toDateString()
      this.isSoon.set(this.isDueSoon());
      this.openDate = false
    }

  }

  updateErrorMessage() {
    if(this.time.hasError('required')){
      this.errorMessage.set('You must enter a value')
    }
  }

  testLabel(color: string) {
    return this.card.labels.some((l) => l.color === color)
  }

  addLabelFromCard(label: Label) {
    this.card.labels.push(label);
    this.cardService.addLabelToCard(this.card.id,label).subscribe({
      next: () => {
        console.log("test réussi !")
      },
      error: (err) => {
        console.error("error : " + err)
      }
    });
  }

  removeLabel(label : Label) {
    this.card.labels = this.card.labels.filter((l) => l.color !== label.color)
    this.cardService.removeLabelToCard(this.card.id,label).subscribe({
      next: () => {
        console.log("test réussi !")
      },
      error: (err) => {
        console.error("error : " + err)
      }
    })
  }

  confirmTitle(title: string,card: Card) {
   this.card.name = title;
    this.cardService.updateCard(this.card.id, card).subscribe({
      next: () => {

      },
      error: (err) => {
        console.error('Error : ' + err);
      },
    });
  }

  activateDueDate(card: Card) {
    this.card.isDateActivated = true;
    this.cardService.updateCard(this.card.id, card).subscribe({
      next: () => {

      },
      error: (err) => {
        console.error('Error : ' + err);
      },
    });
  }
}
