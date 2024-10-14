import {Component, inject, Input, OnInit} from '@angular/core';
import {Board} from '../../dao/board';
import {WorkspaceService} from '../../service/workspaces/workspace.service';
import {HttpErrorResponse} from '@angular/common/http';
import {ActivatedRoute} from '@angular/router';

@Component({
  selector: 'app-workspace',
  standalone: true,
  imports: [],
  template: `
    <ul>
    @for(board of boards ; track board.boardId){
         <li>{{ board.boardName}}</li>
  }
    </ul>
  `,
  styleUrl: './workspace.component.css'
})
export class WorkspaceComponent implements OnInit{
  public boards: Board[] = [];
  private workspaceId : number = 0;
  private workspaceService: WorkspaceService = inject(WorkspaceService);
  private route: ActivatedRoute = inject(ActivatedRoute);

  ngOnInit(): void {
    this.workspaceId = this.route.snapshot.params['id'];
    this.getBoards(this.workspaceId);
  }
  public getBoards(workspaceId: number) : void {

    this.workspaceService.getBoards(workspaceId).subscribe({
        next: (response: Board[]) => this.boards = response,
        error: (error: HttpErrorResponse) => {
          alert("error -> " + error.message)
        }
      }
    )
  }




}
