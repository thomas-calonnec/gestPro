import {Component, computed, inject, OnInit} from '@angular/core';
import {Board} from '../../dao/board';
import {WorkspaceService} from '../../service/workspaces/workspace.service';
import {HttpErrorResponse} from '@angular/common/http';
import {ActivatedRoute, RouterLink} from '@angular/router';
import {BoardService} from '../../service/boards/board.service';

@Component({
  selector: 'app-workspace',
  standalone: true,
  imports: [
    RouterLink
  ],
  template: `
    <div class="containerBoard">
      @for(board of this.boards(); track board.id){
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
  private route: ActivatedRoute = inject(ActivatedRoute);
  boardService = inject(BoardService)

  public boards  = computed(() => this.boardService.getBoards()());


  ngOnInit(): void {
    this.workspaceId = this.route.snapshot.params['workspaceId'];
    this.getBoards(this.workspaceId);
  }
  public getBoards(workspaceId: string | null) : void {

    this.workspaceService.getBoards(workspaceId).subscribe({
        next: (data: Board[]) => {
          this.boardService.setBoards(data)
        },
        error: (error: HttpErrorResponse) => {
          alert("error -> " + error.message)
        }
      }
    )
  }




}
