import {Injectable, signal, WritableSignal} from '@angular/core';
import {Workspace} from '@models/workspace';
import {Board} from '@models/board';

@Injectable({
  providedIn: 'root'
})
export class MainService {

  public workspace : Workspace | undefined;
  public boards : WritableSignal<Board[][]> = signal<Board[][]>([]);
 //public boards : Board[] = []
  public setWorkspace(workspace : Workspace) {
    this.workspace = workspace;
  }
  public getWorkspace() : Workspace | undefined{
    return this.workspace;
  }

  setBoards(boards : Board[]) {
   // this.boards.bind(boards);
    this.boards().push(boards);
  }
  getListBoards(): Board[][] {
    return this.boards();
  }

  removeListBoard() {
    this.boards().pop();
  }
}
