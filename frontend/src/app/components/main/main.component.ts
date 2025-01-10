import {Component, inject, OnInit} from '@angular/core';
import {ActivatedRoute, RouterOutlet} from "@angular/router";
import {AuthService} from '../../auth.service';
import {FormsModule} from '@angular/forms';
import { FontAwesomeModule} from '@fortawesome/angular-fontawesome';
import {WorkspaceService} from '@/services/workspaces/workspace.service';
import {MainService} from '@/services/main/main.service';
import {Workspace} from '@/models/workspace';
import {SidebarComponent} from '../sidebar/sidebar.component';

@Component({
  selector: 'app-main',
  standalone: true,
  imports: [

    RouterOutlet,
    FormsModule,

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
