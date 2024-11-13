import {Component, inject, OnInit, signal, WritableSignal} from '@angular/core';
import {Board} from '../../../dao/board';
import {WorkspaceService} from '../../../service/workspaces/workspace.service';
import {HttpErrorResponse} from '@angular/common/http';
import {ActivatedRoute, RouterLink} from '@angular/router';
import {MainService} from '../../../service/main/main.service';
import {BoardService} from '../../../service/boards/board.service';

@Component({
  selector: 'app-workspace',
  standalone: true,
  imports: [
    RouterLink
  ],
  template: `
    <div class="containerBoard">
      @for(board of this.mainService.boards()[0]; track board.id){
        <div class="hover-card">
          <a style=" text-decoration: none;" routerLink="/boards/{{board.id}}"><h3 class="card-title">{{ board.name }}</h3></a>
        </div>

      } </div>

  `,
  styleUrl: './workspace.component.css'
})
export class WorkspaceComponent implements OnInit{

  private workspaceId : string | null = "null";
   workspaceService: WorkspaceService = inject(WorkspaceService);
   mainService: MainService = inject(MainService)
  boardService: BoardService = inject(BoardService);
  private route: ActivatedRoute = inject(ActivatedRoute);



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
          //this.boardService.boards().push(data);
         // console.log(this.mainService.getListBoards())
        },
        error: (error: HttpErrorResponse) => {
          alert("error -> " + error.message)
        }
      }
    )
  }




}
