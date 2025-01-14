import {Injectable, signal, WritableSignal} from '@angular/core';
import {Board} from '@models/board';

@Injectable({
  providedIn: 'root'
})
export class MainService {
 public  getWorkspaceId(): string | null {
    return localStorage.getItem("workspaceId");
  }

  public setWorkspaceId(value: number) {
    this._workspaceId = value;
    localStorage.setItem("workspaceId",this._workspaceId.toLocaleString());
  }

  public workspace : WritableSignal<string> = signal("");
  private _workspaceId: number = 0;
  public boards : WritableSignal<Board[][]> = signal<Board[][]>([]);
 //public boards : Board[] = []
  public setWorkspace(workspace : string) {
    this.workspace.set(workspace);
    localStorage.setItem("workspaceName",workspace);
  }

  public getWorkspace() :string | null{
    return localStorage.getItem("workspaceName");
  }

  setBoards(boards : Board[]) {
   // this.boards.bind(boards);
    this.boards().push(boards);
  }
  getListBoards(): Board[][] {
    return this.boards();
  }

  removeListBoard() {
    this.boards.set([])
  }
}
