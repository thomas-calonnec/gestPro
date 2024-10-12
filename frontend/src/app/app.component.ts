import {Component, inject, OnInit} from '@angular/core';
import {RouterLink, RouterOutlet} from '@angular/router';
import { Board } from '../dao/board';
import { BoardService } from '../service/boards/board.service';
import { WorkspaceService } from '../service/workspaces/workspace.service';
import { HttpErrorResponse } from '@angular/common/http';
import {BoardComponent} from './board/board.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, RouterLink],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {


}
