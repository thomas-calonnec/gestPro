import {Component, inject, OnInit} from '@angular/core';
import {ActivatedRoute, RouterLink, RouterOutlet} from "@angular/router";
import {AuthService} from '../auth.service';
import {FormsModule} from '@angular/forms';
import {WorkspaceComponent} from '../workspace/workspace.component';
import { FontAwesomeModule} from '@fortawesome/angular-fontawesome';
import {WorkspaceService} from '../../service/workspaces/workspace.service';
import {MainService} from '../../service/main/main.service';
import {Workspace} from '../../dao/workspace';
import {SidebarComponent} from '../sidebar/sidebar.component';

@Component({
  selector: 'app-main',
  standalone: true,
  imports: [
    RouterLink,
    RouterOutlet,
    FormsModule,
    WorkspaceComponent,
    FontAwesomeModule,
    SidebarComponent
  ],
  templateUrl: './main.component.html',
  styleUrl: './main.component.css'
})
export class MainComponent implements OnInit {

  private authService : AuthService = inject(AuthService);
  public workspaceService : WorkspaceService = inject(WorkspaceService);
  public workspaceId: string | null = null;
  private route : ActivatedRoute = inject(ActivatedRoute);
  public mainService = inject(MainService)
  public workspace : Workspace | undefined;

  loggedIn = true

  ngOnInit() {

    //this.workspaceId = this.route.snapshot.paramMap.get('workspaceId');
    //this.boards = this.mainService.getListBoards();
    this.route.firstChild?.paramMap.subscribe((param) => {
      this.workspaceId = param.get('id');
    })


  }


  logout() {
    this.loggedIn = false;
    this.authService.logout();
  }


}
