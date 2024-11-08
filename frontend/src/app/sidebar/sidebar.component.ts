import {Component, inject, Input, input, OnInit, signal, WritableSignal} from '@angular/core';
import {RouterLink} from '@angular/router';
import {AuthService} from '../auth.service';
import {BoardService} from '../../service/boards/board.service';
import {MainService} from '../../service/main/main.service';
import {BoardComponent} from '../board/board.component';
import {WorkspaceService} from '../../service/workspaces/workspace.service';
import {Board} from '../../dao/board';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [
    RouterLink,
    BoardComponent
  ],
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.css'
})
export class SidebarComponent implements OnInit{

  private authService : AuthService = inject(AuthService);
  workspaceService: WorkspaceService = inject(WorkspaceService);
  mainService : MainService = inject(MainService);


  ngOnInit() {
    this.getBoards()
  }
  getBoards() {
    const workspaceId = localStorage.getItem('workspaceId');
    console.log("workspaceId : " + workspaceId)
    if(workspaceId !== null) {
      this.workspaceService.getBoards(workspaceId).subscribe({
          next: (data: Board[]) => {
            this.mainService.setBoards(data);

          }
        }
      )
    }

  }

}
