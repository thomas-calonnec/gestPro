import {Component, inject, OnInit} from '@angular/core';
import {Workspace} from '../../dao/workspace';
import {UserService} from '../../service/users/user.service';
import {ActivatedRoute, RouterLink} from '@angular/router';

@Component({
  selector: 'app-user',
  standalone: true,
  imports: [
    RouterLink
  ],
  template: `
    <ul>
    @for(workspace of workspaces; track workspace.workspaceId){
        <li><a routerLink="/workspaces/{{workspace.workspaceId}}/boards">{{ workspace.workspaceName}}</a></li>
    }</ul>`,
  styleUrl: './user.component.css'
})
export class UserComponent implements  OnInit{
  workspaces : Workspace[] = [];
  userId: number = 0;
  userService : UserService = inject(UserService);
  route : ActivatedRoute = inject(ActivatedRoute);

  ngOnInit(): void{
    this.userId = this.route.snapshot.params['id'];
    this.getWorkspaces();

  }

  getWorkspaces(): void {

    this.userService.getWorkspaces(this.userId).subscribe({
      next: (workspaces: Workspace[]) => {
        this.workspaces = workspaces;
      },
      error: err => {console.error(err)}
    })
  }


}
