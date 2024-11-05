import {Component, inject, OnInit} from '@angular/core';
import {RouterLink} from '@angular/router';
import {Board} from '../../dao/board';
import {BoardService} from '../../service/boards/board.service';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [
    RouterLink
  ],
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.css'
})
export class SidebarComponent {
  boardService : BoardService = inject(BoardService);

  boards = this.boardService.boards;



}
