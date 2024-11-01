import {Component, computed, inject, OnInit} from '@angular/core';
import {Board} from '../../dao/board';
import {WorkspaceService} from '../../service/workspaces/workspace.service';
import {HttpErrorResponse} from '@angular/common/http';
import {ActivatedRoute} from '@angular/router';
import {BoardService} from '../../service/boards/board.service';

@Component({
  selector: 'app-workspace',
  standalone: true,
  imports: [],
  template: `
    <ul>
    @for(board of this.boards() ; track board.id){
         <li>{{ board.name}}</li>
  }
    </ul>
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
