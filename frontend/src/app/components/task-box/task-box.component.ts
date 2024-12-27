import {Component, EventEmitter, Input, Output} from '@angular/core';
import {MatButton} from '@angular/material/button';
import {MatInputModule} from '@angular/material/input';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-task-box',
  standalone: true,
  imports: [
    MatButton,
    MatInputModule,
    FormsModule
  ],
  templateUrl: './task-box.component.html',
  styleUrl: './task-box.component.css'
})
export class TaskBoxComponent {
  @Input() title: string = 'Modal Title';
  @Output() confirm = new EventEmitter<void>();
  @Output() cancel = new EventEmitter<void>();
  @Output() confirmTitle = new EventEmitter<string>();
  modifyTitle: boolean = false;

  closeModal() {
    this.cancel.emit();
  }

  onConfirm() {
    this.confirm.emit();
  }

  modifyTitleConfirm() {
    this.modifyTitle = false
    this.confirmTitle.emit(this.title)
  }
}
