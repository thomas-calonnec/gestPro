import {Component, inject, OnInit} from '@angular/core';
import {RouterLink} from '@angular/router';
import {MainService} from '../../../service/main/main.service';
import {WorkspaceService} from '../../../service/workspaces/workspace.service';
import {Board} from '../../../dao/board';

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
  workspaceId: string | null= ""

  ngOnInit() {
    this.getBoards()
  }
  getBoards() {
    this.workspaceId = localStorage.getItem('workspaceId');

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
