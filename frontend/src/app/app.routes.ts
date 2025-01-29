import { Routes } from '@angular/router';
import { BoardComponent } from '@components/board/board.component';
import { ListCardComponent } from '@components/list-card/list-card.component';
import { WorkspaceComponent } from '@components/workspace/workspace.component';
import { LoginComponent } from '@components/login/login.component';
import { UserComponent } from '@components/user/user.component';
import { MainComponent } from '@components/main/main.component';
import { AuthGuard } from './auth-guard';


export const routes: Routes = [
  {
    path: '', component: MainComponent,  children: [
      { path: 'workspaces/:id/boards', component: WorkspaceComponent, data: { id: 'string' } },
      { path: 'boards/:id', component: BoardComponent, data: { id: 'string' } },
      { path: 'board', component: BoardComponent },
      { path: 'users/:userId/workspaces', component: UserComponent, data: { userId: 'string' } },
      { path: 'boards/:id/listCards', component: ListCardComponent, data: { id: 'string' } },
      { path: 'listCard', component: ListCardComponent }
    ]
  },
  { path: 'login', component: LoginComponent }

];
