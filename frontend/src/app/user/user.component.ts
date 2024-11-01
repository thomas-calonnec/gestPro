import {Component, EventEmitter, inject, OnInit, Output} from '@angular/core';
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
    @for(workspace of workspaces; track workspace.id){
        <li><a routerLink="/workspaces/{{workspace.id}}/boards">{{ workspace.name}}</a></li>
    }</ul>`,
  styleUrl: './user.component.css'
})
export class UserComponent implements  OnInit{
  workspaces : Workspace[] = [];
  userId: string = "";
  userService : UserService = inject(UserService);
  route : ActivatedRoute = inject(ActivatedRoute);
  @Output() paramId  = new EventEmitter<string>();

  ngOnInit(): void{
    this.userId = this.route.snapshot.params['userId'];
    this.paramId.emit(this.userId);
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
