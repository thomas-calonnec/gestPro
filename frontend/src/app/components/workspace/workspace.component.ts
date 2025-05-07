import {Component, inject, LOCALE_ID, model, OnInit, signal, WritableSignal} from '@angular/core';
import {Board} from '@models/board';
import {WorkspaceService} from '@services/workspaces/workspace.service';
import {HttpErrorResponse} from '@angular/common/http';
import {ActivatedRoute, RouterLink} from '@angular/router';
import {MainService} from '@services/main/main.service';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {MatButtonModule} from '@angular/material/button';
import {MatDialog} from '@angular/material/dialog';
import {
  DialogAnimationsExampleDialogComponent
} from '@components/dialog-animations-example-dialog/dialog-animations-example-dialog.component';
import {BoardService} from '@services/boards/board.service';
import {DatePipe, registerLocaleData} from '@angular/common';
import {DialogCreateBoardComponent} from '@components/dialog-create-board/dialog-create-board.component';
import { provideNativeDateAdapter} from '@angular/material/core';
import localeFr from '@angular/common/locales/fr';
import {
  differenceInDays,
  differenceInHours,
  differenceInMinutes,
  differenceInSeconds,
  differenceInWeeks
} from 'date-fns';
import {UserService} from '@services/users/user.service';

registerLocaleData(localeFr);

@Component({
    selector: 'app-workspace',
    templateUrl: 'workspace.component.html',
    providers: [provideNativeDateAdapter(), DatePipe, { provide: LOCALE_ID, useValue: 'fr-FR' },
    ],
    imports: [
        RouterLink,
        ReactiveFormsModule,
        MatButtonModule,
    ],
    styleUrl: './workspace.component.css'
})
export class WorkspaceComponent implements OnInit{

  private workspaceId : number = 0;
  workspaceService: WorkspaceService = inject(WorkspaceService);
  mainService: MainService = inject(MainService);
  userService: UserService = inject(UserService);
  workspaceName: string = "";
  private route: ActivatedRoute = inject(ActivatedRoute);
  boardCreated: boolean = false;
  myForm: FormGroup;
  private formBuilder: FormBuilder = inject(FormBuilder);
  boards : WritableSignal<Board[]> = signal([])
  private boardService: BoardService = inject(BoardService);
  board : any;
  readonly name = model('');
  readonly description = model('');
  readonly dialog = inject(MatDialog);
  protected datePipe: DatePipe = inject(DatePipe);

  constructor() {
    this.myForm = this.formBuilder.group({
      name: ['', Validators.required],
      description: ['']

    });
    this.board = {
      id: 1,
      name: 'Cloud Beauty',
      description: 'Une plateforme pour simplifier et optimiser vos projets.',
      color: '#f39c12', // Couleur pour chaque Board
    };


  }

  ngOnInit(): void {

    this.workspaceId = this.route.snapshot.params['id'];

    this.getWorkspace();
    this.getBoards(this.workspaceId);
    this.mainService.setWorkspaceId(Number(this.workspaceId))


  }
  public getWorkspace() {
    this.workspaceService.getWorkspaceById(this.workspaceId).subscribe({
      next: workspace => {
        this.mainService.setWorkspace(workspace.name)
        // this.workspaceName = workspace.name;
      }
    })

  }

  public getBoards(workspaceId: number) : void {

    this.workspaceService.getBoards(workspaceId).subscribe({
        next: (data: Board[]) => {
          //this.boardService.updateBoards(data);
          this.boards.set(data)

          this.boards.update(boardTab => boardTab.map((board) => {
            const updateAt = new Date(board.lastUpdated);
            const now = new Date();

            const diffWeeks = differenceInWeeks(now,updateAt);
            const diffDays = differenceInDays(now,updateAt);
            const diffHours = differenceInHours(now,updateAt);
            const diffMinutes = differenceInMinutes(now,updateAt);
            const diffSeconds = differenceInSeconds(now,updateAt);
            return {
              ...board,
              weeksSinceUpdate: diffWeeks,
              daysSinceUpdate: diffDays,
              hoursSinceUpdate: diffHours,
              minutesSinceUpdate: diffMinutes,
              secondsSinceUpdate: diffSeconds
            }
          }))
          this.mainService.setBoards(data);
        },
        error: (error: HttpErrorResponse) => {
          alert("error -> " + error.message)
        }
      }
    )
  }

  openDialog(enterAnimationDuration: string, exitAnimationDuration: string, board : Board): void {
   const name = board.name;
   const type = "board";
    const dialogRef = this.dialog.open(DialogAnimationsExampleDialogComponent, {
      width: '290px',
      enterAnimationDuration,
      exitAnimationDuration,
      data: { name, type}
    });

   dialogRef.afterClosed().subscribe(result => {

     if(result && board.id !== undefined) {
       this.boardService.deleteBoardById(board.id).subscribe({
         next: () => {
           //this.router.href = "http://localhost:4200/workspaces/"+this.workspaceId+"/boards";
           window.location.href = "http://localhost:4200/workspaces/"+this.workspaceId+"/boards"
         },
         error: err => {
           console.error("test false : ", err)
         }
       })
     }
   })

  }

  createBoardDialog() {
    const dialogRef = this.dialog.open(DialogCreateBoardComponent, {
      data: {name: this.name(), description: this.description()},
    });

    dialogRef.afterClosed().subscribe((result : Board) => {

      if (result !== undefined) {
        this.datePipe.transform(result.lastUpdated, 'dd/MM/yyyy','fr')
        this.workspaceService.createBoard(this.workspaceId, result).subscribe({
          next:boardValue => {
            this.boards.update((currentValue) => [...currentValue,boardValue])

            window.location.href = "http://localhost:4200/workspaces/"+this.workspaceId+"/boards"
          }
        });

      }
    });
  }
}
