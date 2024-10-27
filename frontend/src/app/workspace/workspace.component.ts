import {Component, inject, OnInit, signal, WritableSignal} from '@angular/core';
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
    @for(board of boards() ; track board.id){
         <li>{{ board.name}}</li>
  }
    </ul>
  `,
  styleUrl: './workspace.component.css'
})
export class WorkspaceComponent implements OnInit{


  boards : WritableSignal<Board[]> = signal<Board[]>([]);

  private workspaceId : number = 0;
  private workspaceService: WorkspaceService = inject(WorkspaceService);
  private route: ActivatedRoute = inject(ActivatedRoute);

  ngOnInit(): void {
    this.workspaceId = this.route.snapshot.params['id'];
    this.getBoards(this.workspaceId);
  }
  public getBoards(workspaceId: number) : void {

    this.workspaceService.getBoards(workspaceId).subscribe({
        next: (data: Board[]) => this.boards.set(data),
        error: (error: HttpErrorResponse) => {
          alert("error -> " + error.message)
        }
      }
    )
  }




}
