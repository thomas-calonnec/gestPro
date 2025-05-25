import {Component, inject, signal, WritableSignal} from '@angular/core';
import {Router, RouterLink, RouterLinkActive} from '@angular/router';
import {FormsModule} from '@angular/forms';
import {LucideAngularModule} from 'lucide-angular';
import {NgOptimizedImage} from '@angular/common';
import {BoardService} from '@services/boards/board.service';

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
  boardService : BoardService = inject(BoardService);
  workspaceName: WritableSignal<string> = signal("");
  boards = this.boardService.boards;
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
