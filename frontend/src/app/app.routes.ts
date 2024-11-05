import { Routes } from '@angular/router';
import {BoardComponent} from './board/board.component';
import {ListCardComponent} from './list-card/list-card.component';
import {WorkspaceComponent} from './workspace/workspace.component';
import { LoginComponent } from './login/login.component';
import {UserComponent} from './user/user.component';
import {MainComponent} from './main/main.component';
import {AuthGuard} from './auth-guard';

export const routes: Routes = [
  {path: '',component: MainComponent, canActivate: [AuthGuard], children : [
      {path: 'workspaces/:id/boards', component: WorkspaceComponent, data: { id: 'string'}},
      {path: 'boards/:id', component: BoardComponent, data: { id: 'string'}},
      {path: 'board', component: BoardComponent},
      {path: 'users/:userId/workspaces', component: UserComponent, data: {userId: 'string'}},
      {path: 'boards/:id/listCards', component: ListCardComponent, data: {id: 'string'}},
      {path: 'listCard', component: ListCardComponent}
    ]},
  {path: 'login', component: LoginComponent}

];
