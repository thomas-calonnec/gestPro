import { Routes } from '@angular/router';
import {BoardComponent} from './board/board.component';
import {ListCardComponent} from './list-card/list-card.component';
import {WorkspaceComponent} from './workspace/workspace.component';
import { LoginComponent } from './login/login.component';
import {UserComponent} from './user/user.component';

export const routes: Routes = [
  {path: '', redirectTo: '/login', pathMatch: 'full'},
  {path: 'workspaces/:id/boards', component: WorkspaceComponent, data: { id: 'string'}},
  {path: 'board', component: BoardComponent},
  {path: 'login', component: LoginComponent},
  {path: 'users/:id/workspaces', component: UserComponent, data: {id: 'string'}},
  {path: 'listCard', component: ListCardComponent}
];
