import {Component, inject, OnInit, signal, WritableSignal} from '@angular/core';
import {Router, RouterLink, RouterLinkActive} from '@angular/router';
import {MainService} from '@services/main/main.service';
import {WorkspaceService} from '@services/workspaces/workspace.service';
import {Board} from '@models/board';
import {AuthService} from '@services/auth/auth.service';
import {FormsModule} from '@angular/forms';
import {LucideAngularModule,LayoutDashboard} from 'lucide-angular';
import {NgOptimizedImage} from '@angular/common';

@Component({
    selector: 'app-sidebar',
  imports: [
    RouterLink,
    FormsModule,
    LucideAngularModule,
    RouterLinkActive,
    NgOptimizedImage
  ],
    templateUrl: './sidebar.component.html',
    styleUrl: './sidebar.component.scss'
})
export class SidebarComponent {
  workspaceService: WorkspaceService = inject(WorkspaceService);
  mainService : MainService = inject(MainService);
  workspaceName: WritableSignal<string> = signal("");
  boards = this.workspaceService.boards;
  router: Router = inject(Router);
  searchTerm = '';
  showAll = false;
  maxVisible = 3;

    public filteredBoards() {

    return this.boards().filter(board =>
      board.name.toLowerCase().includes(this.searchTerm.toLowerCase())
    );
  }


  limitedBoards() {
    const filtered = this.filteredBoards();
    return this.showAll ? filtered : filtered.slice(0, this.maxVisible);
  }


  protected readonly localStorage = localStorage;
}
