import {Component, inject, Input, OnInit} from '@angular/core';
import {MatDialog} from '@angular/material/dialog';
import {TaskBoxComponent} from '../task-box/task-box.component';
import {MatButton} from '@angular/material/button';

@Component({
  selector: 'app-card',
  standalone: true,
  imports: [
    MatButton
  ],
  template:`<div class="card-content">
    <!--  <div class="status-badges">
        <span class="badge badge-green">d</span>
        <span class="badge badge-yellow">Oct 4</span>
        <span class="badge badge-blue">6/6</span>
      </div>-->
    @if(this.isHover) {
      <button mat-raised-button color="accent" (click)="openDialog()"><div (mouseenter)="this.hover()" (mouseleave)="isHover = false"
                                                                           class="task-name">{{ taskName }}<i class="fa-regular fa-pen-to-square" style=" text-align: right;"></i></div></button>
    } @else {
    <a ><div (mouseenter)="isHover=true" (mouseleave)="isHover = false"  class="task-name">{{ taskName }}</div></a>
    }
    @if(this.isClicked) {
      <p>test</p>
     <!-- <div class="modal fade"  tabindex="-1" role="dialog" >
        <div class="modal-dialog" role="document">
          <div class="modal-content">
            <div class="modal-header">
              <h5 class="modal-title" id="exampleModalLabel">New message</h5>
              <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                <span aria-hidden="true">&times;</span>
              </button>
            </div>
            <div class="modal-body">
              <form>
                <div class="form-group">
                  <label for="recipient-name" class="col-form-label">Recipient:</label>
                  <input type="text" class="form-control" id="recipient-name">
                </div>
                <div class="form-group">
                  <label for="message-text" class="col-form-label">Message:</label>
                  <textarea class="form-control" id="message-text"></textarea>
                </div>
              </form>
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
              <button type="button" class="btn btn-primary">Send message</button>
            </div>
          </div>
        </div>
      </div>-->

    }
  </div>

  `,
  styleUrl: './card.component.css'
})
export class CardComponent implements OnInit {
@Input() taskName = "";
protected isHover = false;
protected isClicked = false;
protected dialog: MatDialog = inject(MatDialog);

  ngOnInit(): void {
  }
  hover() {
    this.isHover = true;
  }

  openDialog() {
    const dialogRef = this.dialog.open(TaskBoxComponent, {
      width: '480px',
      disableClose: true
    })
  }
  clickModal() {
    this.isClicked = true;
    console.log(this.isClicked)
  }
}
