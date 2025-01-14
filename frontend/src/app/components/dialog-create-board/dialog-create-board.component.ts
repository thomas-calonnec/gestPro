import {Component, Inject, inject, model} from '@angular/core';
import {
  MAT_DIALOG_DATA,
  MatDialogActions,
  MatDialogClose,
  MatDialogContent,
  MatDialogRef,
  MatDialogTitle
} from '@angular/material/dialog';
import { MatFormFieldModule} from '@angular/material/form-field';
import {FormsModule} from '@angular/forms';
import {MatInput} from '@angular/material/input';
import {MatButton} from '@angular/material/button';
import {Board} from '@models/board';

@Component({
  selector: 'app-dialog-create-board',
  standalone: true,
  imports: [
    MatDialogContent,
    MatDialogTitle,
    MatFormFieldModule,
    MatDialogActions,
    MatDialogClose,
    FormsModule,
    MatInput,
    MatButton
  ],
  templateUrl: './dialog-create-board.component.html',
  styleUrl: './dialog-create-board.component.css'
})
export class DialogCreateBoardComponent {
  readonly dialogRef = inject(MatDialogRef<DialogCreateBoardComponent>);
   readonly board:Board = {
    id: 0,
    name: '',
    color: '',
    description: '',
    lastUpdated: new Date(),
    cardCount: 0
  }
  readonly addBoard = model(this.board)
  constructor(@Inject(MAT_DIALOG_DATA) public data: { name: string, description : string }) {}

  onNoClick(): void {
    this.dialogRef.close();
  }
}

