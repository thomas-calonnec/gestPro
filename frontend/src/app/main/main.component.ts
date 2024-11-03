import {Component, computed, inject, OnInit} from '@angular/core';
import {ActivatedRoute, RouterLink, RouterOutlet} from "@angular/router";
import {AuthService} from '../auth.service';
import {FormsModule} from '@angular/forms';
import {BoardService} from '../../service/boards/board.service';
import {WorkspaceComponent} from '../workspace/workspace.component';
import { FontAwesomeModule} from '@fortawesome/angular-fontawesome';
import {faTable, faUser, faPerson, faUsers, faGear} from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-main',
  standalone: true,
  imports: [
    RouterLink,
    RouterOutlet,
    FormsModule,
    WorkspaceComponent,
    FontAwesomeModule
  ],
  templateUrl: './main.component.html',
  styleUrl: './main.component.css'
})
export class MainComponent implements OnInit {
  faTable = faTable
  private authService : AuthService = inject(AuthService);
  public workspaceId: string | null = null;
  private route : ActivatedRoute = inject(ActivatedRoute);
  public boardService = inject(BoardService)
  public boards  = computed(() => this.boardService.getBoards()());
  loggedIn = true


  ngOnInit() {

    //this.workspaceId = this.route.snapshot.paramMap.get('workspaceId');

    this.route.firstChild?.paramMap.subscribe((param) => {
      this.workspaceId = param.get('workspaceId');

    })

    this.loggedIn = true;
  }



  logout() {
    this.loggedIn = false;
    this.boards().pop();
    this.authService.logout();
  }


  protected readonly faPerson = faPerson;
  protected readonly faUser = faUser;
  protected readonly faUsers = faUsers;
  protected readonly faGear = faGear;
}
