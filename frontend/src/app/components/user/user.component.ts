import {Component, EventEmitter, inject, OnInit, Output, signal, WritableSignal} from '@angular/core';
import {Workspace} from '@models/workspace';
import {UserService} from '@services/users/user.service';
import {ActivatedRoute, RouterLink} from '@angular/router';
import {MainService} from '@services/main/main.service';
import {MatButton} from '@angular/material/button';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';

import {WorkspaceService} from '@services/workspaces/workspace.service';
import {MatDialog} from '@angular/material/dialog';
import {
  DialogAnimationsExampleDialogComponent
} from '@components/dialog-animations-example-dialog/dialog-animations-example-dialog.component';

@Component({
    selector: 'app-user',
    templateUrl: 'user.component.html',
    imports: [
        RouterLink,
        MatButton,
        ReactiveFormsModule
    ],
    styleUrl: './user.component.css'
})
export class UserComponent implements  OnInit{
  workspaces : WritableSignal<Workspace[]> = signal([]);
  userId: string = "";
  userService : UserService = inject(UserService);
  route : ActivatedRoute = inject(ActivatedRoute);
  mainService : MainService = inject(MainService);
  @Output() paramId  = new EventEmitter<string>();
  workspaceCreated: boolean = false;
  private formBuilder : FormBuilder = inject(FormBuilder);
  myForm: FormGroup;
  readonly dialog = inject(MatDialog);
  private workspaceService: WorkspaceService = inject(WorkspaceService);

  constructor() {
    this.myForm = this.formBuilder.group({
      name: ['', Validators.required]
    })
  }
  ngOnInit(): void{
    this.userId = this.route.snapshot.params['userId'];
    this.paramId.emit(this.userId);
    this.getWorkspaces();
    localStorage.setItem("workspaceName","");
    this.mainService.removeListBoard()

  }

  getWorkspaces(): void {

    this.userService.getWorkspaces(this.userId).subscribe({
      next: (workspaces: Workspace[]) => {
        this.workspaces.set(workspaces);
      },
      error: err => {console.error(err)}
    })
  }


  addWorkspace() {
    const workspace:Workspace =  {
      id: 0,
      name: this.myForm.value.name
    };
    this.userService.createWorkspace(Number(this.userId),workspace).subscribe({
      next: data => {
        this.workspaceCreated = false;
        this.workspaces.update(currentValue => [...currentValue,data]);

      }
    })
  }
  openDialog(enterAnimationDuration: string, exitAnimationDuration: string, workspace : Workspace): void {
    const name = workspace.name;
    const type = "workspace";
    const dialogRef = this.dialog.open(DialogAnimationsExampleDialogComponent, {
      width: '380px',
      enterAnimationDuration,
      exitAnimationDuration,
      data: {name, type}
    });




    dialogRef.afterClosed().subscribe((result: any) => {

      if (result) {
        this.workspaceService.deleteWorkspaceById(workspace.id).subscribe({
          next: () => {
            //this.router.href = "http://localhost:4200/workspaces/"+this.workspaceId+"/boards";

            window.location.href = "http://localhost:4200/users/" + this.userId + "/workspaces"
          },
          error: err => {
            console.error("test false : ", err)
          }
        })
      }
    })
  }
}
