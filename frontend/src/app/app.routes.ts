import { Routes } from '@angular/router';
import {BoardComponent} from './board/board.component';
import {ListCardComponent} from './list-card/list-card.component';
import {WorkspaceComponent} from './workspace/workspace.component';
import { LoginComponent } from './login/login.component';

export const routes: Routes = [
  {path: '', redirectTo: '/login', pathMatch: 'full'},
  {path: 'workspaces/:id/boards', component: WorkspaceComponent, data: { id: 'string'}},
  {path: 'board', component: BoardComponent},
  {path: 'login', component: LoginComponent},
  //{path: 'board/:id', component: BoardComponent},
  {path: 'listCard', component: ListCardComponent}
];
