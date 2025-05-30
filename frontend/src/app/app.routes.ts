import { Routes } from '@angular/router';
import { BoardComponent } from '@components/board/board.component';
import { ListCardComponent } from '@components/list-card/list-card.component';
import { LoginComponent } from '@components/login/login.component';
import { UserComponent } from '@components/user/user.component';
import { MainComponent } from '@components/main/main.component';
import { CallbackComponent} from '@components/callback/callback.component';
import { AuthGuard } from './auth-guard';
import {HomeComponent} from '@components/home/home.component';
import {ListBoardsComponent} from '@components/list-boards/list-boards.component';
import {InvitationsComponent} from '@components/invitation/invitation.component';


export const routes: Routes = [
  {
    path: '', component: MainComponent, canActivate:[AuthGuard], children: [
      {path: 'home', component: HomeComponent},
      { path: 'workspaces/:id/boards', component: ListBoardsComponent, data: { id: 'string' } },
      { path: 'boards/:id', component: BoardComponent, data: { id: 'string' } },
      // { path: 'board', component: BoardComponent },
      {path: 'invitations',component: InvitationsComponent},
      { path: 'users/:userId/workspaces', component: UserComponent, data: { userId: 'string' } },
      { path: 'boards/:id/listCards', component: ListCardComponent, data: { id: 'string' } },
      { path: 'listCard', component: ListCardComponent },

    ]
  },
  { path: 'login', component: LoginComponent },
  { path: 'callback', component: CallbackComponent }

];
