import {Component, inject, OnInit} from '@angular/core';
import {Workspace} from '../../dao/workspace';
import {UserService} from '../../service/users/user.service';
import {ActivatedRoute, RouterLink} from '@angular/router';
import {HttpResponse} from '@angular/common/http';

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

    //this.getToken()

  }
   parseJwt (token: string) {
    const base64Url = token.split('.')[1]; // Extraire la partie Payload du token JWT
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    const jsonPayload = decodeURIComponent(atob(base64).split('').map(function(c) {
      return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
    }).join(''));

    return JSON.parse(jsonPayload); // Retourner le JSON extrait
  }
  getToken(): void {
    this.userService.getProtectedData().subscribe({
      next: (response: String) => {
        console.log(response);

      }
    })
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
