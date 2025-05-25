import {Component, EventEmitter, inject, model, OnInit, Output, signal, WritableSignal} from '@angular/core';
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
import {
  differenceInDays,
  differenceInHours,
  differenceInMinutes,
  differenceInSeconds,
  differenceInWeeks
} from 'date-fns';
import {WorkspaceComponent} from '@components/workspace/workspace.component';
import {BoardService} from '@services/boards/board.service';

@Component({
    selector: 'app-user',
    templateUrl: 'user.component.html',
  imports: [
    RouterLink,
    MatButton,
    ReactiveFormsModule,
    WorkspaceComponent
  ],
    styleUrl: 'user.component.css'
})
export class UserComponent implements  OnInit{
  workspaces : WritableSignal<Workspace[]> = signal([]);
  userId: string = "";
  userService : UserService = inject(UserService);
  route : ActivatedRoute = inject(ActivatedRoute);
  mainService : MainService = inject(MainService);
  boardService :BoardService = inject(BoardService);
  nbBoard: number = 0;
  @Output() paramId  = new EventEmitter<string>();
  workspaceCreated: boolean = false;
  favorite = model<boolean>(false);
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
    localStorage.setItem("workspaceId","");
    this.boardService.setBoards([]);
    this.mainService.removeListBoard();

  }

  getWorkspaces(): void {

    this.workspaceService.getWorkspaces(this.userId).subscribe({
      next: (workspaces: Workspace[]) => {
        this.workspaces.set(workspaces);
        this.workspaces.update(workspaceTab => workspaceTab.map((workspace) => {
          const updateAt = new Date(workspace.updateAt);
          const now = new Date();
          const diffWeeks = differenceInWeeks(now,updateAt);
          const diffDays = differenceInDays(now,updateAt);
          const diffHours = differenceInHours(now,updateAt);
          const diffMinutes = differenceInMinutes(now,updateAt);
          const diffSeconds = differenceInSeconds(now,updateAt);
          return {
            ...workspace,
            weeksSinceUpdate: diffWeeks,
            daysSinceUpdate: diffDays,
            hoursSinceUpdate: diffHours,
            minutesSinceUpdate: diffMinutes,
            secondsSinceUpdate: diffSeconds
          }
        }))
      },
      error: err => {console.error(err)}
    })
  }


  addWorkspace() {
    const workspace:Workspace =  {
      id: 0,
      name: this.myForm.value.name,
      description: "",
      updateAt: new Date(),
      weeksSinceUpdate: 0,
      daysSinceUpdate: 0,
      hoursSinceUpdate: 0,
      minutesSinceUpdate: 0,
      secondsSinceUpdate: 0,
      isFavorite: false,
      boards: []
    };
    this.workspaceService.createWorkspace(Number(this.userId),workspace).subscribe({
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

  setFavorite(workspaceId: number) {
    this.workspaces.update(workspaces => {
      // Mettre à jour le workspace dont l'id correspond à workspaceId
      const updatedWorkspaces = workspaces.map(workspace => {
        if (workspace.id == workspaceId) {
          return {
            ...workspace,
            isFavorite: !workspace.isFavorite // Met à jour l'attribut isFavorite uniquement pour celui-là
          };
        }
        return workspace; // Les autres workspaces restent inchangés
      });

      // Trouver le workspace modifié pour le renvoyer uniquement (si besoin)
      const updatedWorkspace = updatedWorkspaces.find(workspace => workspace.id == workspaceId);

      // Tu peux maintenant utiliser updatedWorkspace ou envoyer une action pour l'update
       this.workspaceService.updateWorkspace(workspaceId, updatedWorkspace)

      return updatedWorkspaces; // Retourner le tableau mis à jour
    });
  }
}


