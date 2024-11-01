import {Component, computed, inject, OnInit} from '@angular/core';
import {ActivatedRoute, RouterLink, RouterOutlet} from "@angular/router";
import {AuthService} from '../auth.service';
import {FormsModule} from '@angular/forms';
import {BoardService} from '../../service/boards/board.service';
import {WorkspaceComponent} from '../workspace/workspace.component';

@Component({
  selector: 'app-main',
  standalone: true,
  imports: [
    RouterLink,
    RouterOutlet,
    FormsModule,
    WorkspaceComponent
  ],
  templateUrl: './main.component.html',
  styleUrl: './main.component.css'
})
export class MainComponent implements OnInit {

  private authService : AuthService = inject(AuthService);
  private workspaceId: string | null = null;
  private route : ActivatedRoute = inject(ActivatedRoute);
  public boardService = inject(BoardService)
  public boards  = computed(() => this.boardService.getBoards()());
  loggedIn = true


  ngOnInit() {

    //this.workspaceId = this.route.snapshot.paramMap.get('workspaceId');

    this.route.firstChild?.paramMap.subscribe((param) => {
      this.workspaceId = param.get('workspaceId');
      console.log(this.workspaceId)
    })

    console.log("vrai")
    this.loggedIn = true;
  }



  logout() {
    this.loggedIn = false;
    this.boards().pop();
    this.authService.logout();
  }


}
