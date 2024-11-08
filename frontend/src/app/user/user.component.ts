import {Component, EventEmitter, inject, OnInit, Output} from '@angular/core';
import {Workspace} from '../../dao/workspace';
import {UserService} from '../../service/users/user.service';
import {ActivatedRoute, RouterLink} from '@angular/router';
import {MainService} from '../../service/main/main.service';

@Component({
  selector: 'app-user',
  standalone: true,
  imports: [
    RouterLink
  ],
  template: `

<div class="containerWorkspace">
    @for(workspace of workspaces; track workspace.id){
      <div class="hover-card">
        <a style=" text-decoration: none;" routerLink="/workspaces/{{workspace.id}}/boards"><h3 class="card-title">{{ workspace.name }}</h3></a>
      </div>

    } </div>`,
  styleUrl: './user.component.css'
})
export class UserComponent implements  OnInit{
  workspaces : Workspace[] = [];
  userId: string = "";
  userService : UserService = inject(UserService);
  route : ActivatedRoute = inject(ActivatedRoute);
  mainService : MainService = inject(MainService);
  @Output() paramId  = new EventEmitter<string>();

  ngOnInit(): void{
    this.userId = this.route.snapshot.params['userId'];
    this.paramId.emit(this.userId);
    this.getWorkspaces();
    localStorage.removeItem("workspaceId")
    this.mainService.removeListBoard()

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
