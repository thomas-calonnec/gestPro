import {Component, Inject} from '@angular/core';
import {
  MAT_DIALOG_DATA,
  MatDialogActions,
  MatDialogClose,
  MatDialogContent,
  MatDialogTitle
} from '@angular/material/dialog';
import {MatButton} from '@angular/material/button';

@Component({
    selector: 'app-dialog-animations-example-dialog',
    imports: [
        MatDialogTitle,
        MatDialogContent,
        MatDialogActions,
        MatButton,
        MatDialogClose
    ],
    templateUrl: './dialog-animations-example-dialog.component.html',
    styleUrl: './dialog-animations-example-dialog.component.css'
})
export class DialogAnimationsExampleDialogComponent {

  constructor(@Inject(MAT_DIALOG_DATA) public data: { name: string, type : string }) {

  }

}
