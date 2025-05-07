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
  workspaceName: WritableSignal<string> = signal("");
  authService: AuthService = inject(AuthService);
  router: Router = inject(Router);

  ngOnInit() {
    const storedId = localStorage.getItem('workspaceId');

    if (storedId !== null && !isNaN(Number(storedId))) {
      this.mainService.setWorkspaceId(Number(storedId));
      this.getBoards(); // Appeler seulement si l'ID est valide
    } else {
      console.warn('Aucun workspaceId valide dans le localStorage');
      // Optionnel : rediriger ou afficher un message
    }

    this.mainService.removeListBoard();
    //this.getWorkspace();
  }


  getBoards() {

    if(this.mainService.getWorkspaceId() !== null) {
      this.workspaceService.getBoards(this.mainService.getWorkspaceId()).subscribe({
          next: (data: Board[]) => {
            this.mainService.setBoards(data);

          }
        }
      )
    }

  }

  protected readonly localStorage = localStorage;
}
