import { Routes } from '@angular/router';
import {AppComponent} from './app.component';
import {BoardComponent} from './board/board.component';
import {ListCardComponent} from './list-card/list-card.component';
import {WorkspaceComponent} from './workspace/workspace.component';

export const routes: Routes = [
  {path: 'workspace/:id/boards', component: WorkspaceComponent, data: { id: 'string'}},
  {path: 'board', component: BoardComponent},
  //{path: 'board/:id', component: BoardComponent},
  {path: 'listCard', component: ListCardComponent}
];
