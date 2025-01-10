import {Component, inject, OnInit} from '@angular/core';
import {RouterLink} from '@angular/router';
import {MainService} from '@services/main/main.service';
import {WorkspaceService} from '@services/workspaces/workspace.service';
import {Board} from '@models/board';
import {AuthService} from '@app/auth.service';

@Component({
  selector: 'app-sidebar',
  standalone: true,
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
  workspaceName: string = "";
  authService: AuthService = inject(AuthService)

  ngOnInit() {
    this.workspaceId = localStorage.getItem('workspaceId');
    this.getBoards()

  this.getWorkspace()
  }
  getWorkspace() {
    this.workspaceService.getWorkspaceById(this.workspaceId).subscribe({
      next: (workspace) => {
        this.workspaceName = workspace.name;
      }
    })
  }
  logout() {
    this.authService.logout();
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

}
