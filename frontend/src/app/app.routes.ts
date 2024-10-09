import { Routes } from '@angular/router';
import { BoardComponent } from './board/board.component';
import { ListCardComponent } from './list-card/list-card.component';
import { AppComponent } from './app.component';

export const routes: Routes = [
    {path: 'board', component:BoardComponent},
    {path: 'listcard', component: ListCardComponent},
  
];
