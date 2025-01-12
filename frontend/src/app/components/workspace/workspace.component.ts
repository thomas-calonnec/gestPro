import {Component, inject, OnInit, signal, WritableSignal} from '@angular/core';
import {Board} from '@models/board';
import {WorkspaceService} from '@services/workspaces/workspace.service';
import {HttpErrorResponse} from '@angular/common/http';
import {ActivatedRoute, RouterLink} from '@angular/router';
import {MainService} from '@services/main/main.service';
import {BoardService} from '@services/boards/board.service';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {MatButtonModule} from '@angular/material/button';

@Component({
  selector: 'app-workspace',
  standalone: true,
  templateUrl: 'workspace.component.html',
  imports: [
    RouterLink,
    ReactiveFormsModule,
    MatButtonModule
  ],
  styleUrl: './workspace.component.css'
})
export class WorkspaceComponent implements OnInit{

  private workspaceId : string | null = "null";
   workspaceService: WorkspaceService = inject(WorkspaceService);
   mainService: MainService = inject(MainService)
  boardService: BoardService = inject(BoardService);
  private route: ActivatedRoute = inject(ActivatedRoute);
  boardCreated: boolean = false;
  myForm: FormGroup;
  private formBuilder: FormBuilder = inject(FormBuilder);
  boards : WritableSignal<Board[]> = signal([])

  constructor() {
    this.myForm = this.formBuilder.group({
      name: ['', Validators.required],

    });

  }

  ngOnInit(): void {
    this.workspaceId = this.route.snapshot.params['id'];

    this.getBoards(this.workspaceId);

  }
  public getBoards(workspaceId: string | null) : void {

    this.workspaceService.getBoards(workspaceId).subscribe({
        next: (data: Board[]) => {
          //this.boardService.updateBoards(data);
          this.mainService.setBoards(data);
          if (typeof this.workspaceId === "string") {
            localStorage.setItem("workspaceId", this.workspaceId);
          }
          this.boards.set(data)
          //this.boardService.boards().push(data);
         // console.log(this.mainService.getListBoards())
        },
        error: (error: HttpErrorResponse) => {
          alert("error -> " + error.message)
        }
      }
    )
  }


 public addBoard() {
    const board : Board = {
      id: -1,
      name : this.myForm.value.name,
      color: ""

    };
    this.workspaceService.createBoard(this.workspaceId,board).subscribe({
      next: (data: Board ) => {
        this.boardCreated = false;
        this.boards.update((currentCard) => [...currentCard, data]);

      }
    });
  }
}
