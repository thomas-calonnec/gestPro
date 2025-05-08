import {Component, inject, OnInit} from '@angular/core';
import {ActivatedRoute, RouterOutlet} from "@angular/router";
import {FormsModule} from '@angular/forms';
import { FontAwesomeModule} from '@fortawesome/angular-fontawesome';
import {Workspace} from '@models/workspace';
import {SidebarComponent} from '../sidebar/sidebar.component';
import {BoardService} from '@services/boards/board.service';
import {WorkspaceService} from '@services/workspaces/workspace.service';

@Component({
    selector: 'app-main',
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
  public workspaceId: string | null = null;
  private route : ActivatedRoute = inject(ActivatedRoute);
  private workspaceService = inject(WorkspaceService)
  public workspace : Workspace | undefined;

  ngOnInit() {

    this.route.firstChild?.paramMap.subscribe((param) => {
      this.workspaceId = param.get('id');
    })

  }
}
