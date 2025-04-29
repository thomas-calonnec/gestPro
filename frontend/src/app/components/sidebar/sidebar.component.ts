import {Component, inject, OnInit, signal, WritableSignal} from '@angular/core';
import { Router, RouterLink} from '@angular/router';
import {MainService} from '@services/main/main.service';
import {WorkspaceService} from '@services/workspaces/workspace.service';
import {Board} from '@models/board';
import {AuthService} from '@services/auth/auth.service';

@Component({
    selector: 'app-sidebar',
    imports: [
        RouterLink
    ],
    templateUrl: './sidebar.component.html',
    styleUrl: './sidebar.component.css'
})
export class SidebarComponent implements OnInit{
  workspaceService: WorkspaceService = inject(WorkspaceService);
  mainService : MainService = inject(MainService);
  workspaceId: string | null = "";
  workspaceName: WritableSignal<string> = signal("");
  authService: AuthService = inject(AuthService);
  router: Router = inject(Router);

  ngOnInit() {
    this.workspaceId = localStorage.getItem('workspaceId');
    this.getBoards();
    this.mainService.removeListBoard();
    //this.getWorkspace();
  }
  getWorkspace() {
    this.workspaceService.getWorkspaceById(this.workspaceId).subscribe({
      next: (workspace) => {
        this.mainService.setWorkspace(workspace.name)

      }
    })
  }
  logout() {
    window.location.href = 'http://localhost:8080/logout';
  }
  getBoards() {

    if(this.workspaceId !== null) {
      this.workspaceService.getBoards(this.workspaceId).subscribe({
          next: (data: Board[]) => {
            this.mainService.setBoards(data);

          }
        }
      )
    }

  }

  protected readonly localStorage = localStorage;
}
