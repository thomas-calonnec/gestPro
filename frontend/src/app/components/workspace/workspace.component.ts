import {Component, inject, OnInit} from '@angular/core';
import {Board} from '@/models/board';
import {WorkspaceService} from '@/services/workspaces/workspace.service';
import {HttpErrorResponse} from '@angular/common/http';
import {ActivatedRoute, RouterLink} from '@angular/router';
import {MainService} from '@/services/main/main.service';
import {BoardService} from '@/services/boards/board.service';

@Component({
  selector: 'app-workspace',
  standalone: true,
  imports: [
    RouterLink
  ],
  template: `
    <h3>Your boards</h3>
    <div class="containerBoard">
      @for(board of this.mainService.boards()[0]; track board.id){

        <a style=" text-decoration: none;" routerLink="/boards/{{board.id}}"> <div class="hover-card">
         <h3 class="card-title">{{ board.name }}</h3>
        </div>
        </a>

      }
      <div>
        <a style=" text-decoration: none;" href="#"><h3 class="card-title"><i class="fa fa-plus-circle"></i> Add a board </h3></a>
      </div>
    </div>

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
