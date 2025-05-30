import {Component, inject, OnInit, signal, WritableSignal} from '@angular/core';
import {Router, RouterLink, RouterLinkActive} from '@angular/router';
import {FormsModule} from '@angular/forms';
import {LucideAngularModule} from 'lucide-angular';
import {NgOptimizedImage} from '@angular/common';
import {BoardService} from '@services/boards/board.service';
import {InvitationService} from '@services/invitations/invitation.service';

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
export class SidebarComponent implements OnInit{
  boardService : BoardService = inject(BoardService);
  invitationService: InvitationService = inject(InvitationService);
  workspaceName: WritableSignal<string> = signal("");

  invitationCount = 0;
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

  getInvitationByUser() {
    const userId = Number(localStorage.getItem("USER_ID"));
    this.invitationService.getMyInvitations(userId).subscribe({
      next: invitations => {
        this.invitationService.setInvitations(invitations);
        this.invitationCount = this.invitationService.getInvitations().length;
      }
    })
  }

  protected readonly localStorage = localStorage;

  ngOnInit(): void {
    this.getInvitationByUser()
  }
}
