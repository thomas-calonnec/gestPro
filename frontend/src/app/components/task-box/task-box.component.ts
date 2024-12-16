import {Component, EventEmitter, inject, Input, Output} from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import {MatButton} from '@angular/material/button';

@Component({
  selector: 'app-task-box',
  standalone: true,
  imports: [
    MatButton
  ],
  templateUrl: './task-box.component.html',
  styleUrl: './task-box.component.css'
})
export class TaskBoxComponent {
  @Input() title: string = 'Modal Title';
  @Output() confirm = new EventEmitter<void>();
  @Output() cancel = new EventEmitter<void>();

  closeModal() {
    this.cancel.emit();
  }

  onConfirm() {
    this.confirm.emit();
  }
}
