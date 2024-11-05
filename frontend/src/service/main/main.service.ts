import {Injectable, Signal, signal} from '@angular/core';
import {Workspace} from '../../dao/workspace';
import {Board} from '../../dao/board';

@Injectable({
  providedIn: 'root'
})
export class MainService {

  public workspace : Workspace | undefined;
  public boards : Signal<Board[]> = signal<Board[]>([]);
 //public boards : Board[] = []
  public setWorkspace(workspace : Workspace) {
    this.workspace = workspace;
  }
  public getWorkspace() : Workspace | undefined{
    return this.workspace;
  }

  setBoards(boards : Board[]) {
   // this.boards.bind(boards);
    this.boards.bind(boards);
  }
  getListBoards(): Board[] {
    return this.boards();
  }
}
