import {Component, computed, inject, LOCALE_ID, model, OnInit, signal, WritableSignal} from '@angular/core';
import {Board} from '@models/board';
import {WorkspaceService} from '@services/workspaces/workspace.service';
import {HttpErrorResponse} from '@angular/common/http';
import {ActivatedRoute, RouterLink} from '@angular/router';
import {MainService} from '@services/main/main.service';
import {FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import {MatButtonModule} from '@angular/material/button';
import {DatePipe, NgOptimizedImage, registerLocaleData} from '@angular/common';
import { provideNativeDateAdapter} from '@angular/material/core';
import localeFr from '@angular/common/locales/fr';

import {MatChipsModule} from '@angular/material/chips';
import { MatIconModule } from '@angular/material/icon';
import {MatAutocompleteModule, MatAutocompleteSelectedEvent} from '@angular/material/autocomplete';
import { MatFormFieldModule } from '@angular/material/form-field';
import {
  differenceInDays,
  differenceInHours,
  differenceInMinutes,
  differenceInSeconds,
  differenceInWeeks
} from 'date-fns';
import {UserService} from '@services/users/user.service';
import {COMMA, ENTER} from '@angular/cdk/keycodes';
import {User} from '@models/user';

registerLocaleData(localeFr);

@Component({
    selector: 'app-workspace',
    templateUrl: 'workspace.component.html',
    providers: [provideNativeDateAdapter(), DatePipe, { provide: LOCALE_ID, useValue: 'fr-FR' },
    ],
  imports: [
    MatFormFieldModule, MatChipsModule, MatIconModule, MatAutocompleteModule, FormsModule,
    RouterLink,
    ReactiveFormsModule,
    MatButtonModule,
    FormsModule,
    NgOptimizedImage,

  ],
    styleUrl: './workspace.component.css'
})
export class WorkspaceComponent implements OnInit{

  private workspaceId : number = 0;
  private workspaceService: WorkspaceService = inject(WorkspaceService);
   mainService: MainService = inject(MainService);
  private userService: UserService = inject(UserService);
  owner : User | undefined;
  workspaceName: string = "";
  private route: ActivatedRoute = inject(ActivatedRoute);
  boardCreated: boolean = false;
  myForm: FormGroup;
  boards : WritableSignal<Board[]> = signal([])
  board : any;
  readonly name = model('');
  readonly description = model('');
  protected datePipe: DatePipe = inject(DatePipe);
  searchTerm = '';
  readonly separatorKeysCodes = [ENTER, COMMA] as const;
  currentUser = new FormControl('');
  private _users = signal<User[]>([]);
  allUsers = signal<User[]>([]);
  users = this._users.asReadonly();
  readonly filteredUser = computed(() => {
    const currentUser = this.currentUser.get('currentUser')?.value.toLowerCase();
    return currentUser
      ? this.allUsers().filter(member => member.username.toLowerCase().includes(currentUser))
      : this.allUsers().slice();
  });
  constructor() {
    this.myForm = new FormGroup({
      name: new FormControl('',[Validators.required]),
      description: new FormControl('',[Validators.required]),
      selectedItems: new FormControl([]) // chips choisies
    });

    this.board = {
      id: 1,
      name: 'Cloud Beauty',
      description: 'Une plateforme pour simplifier et optimiser vos projets.',
      color: '#f39c12', // Couleur pour chaque Board
    };

  }

  ngOnInit(): void {
    this.workspaceId = this.route.snapshot.params['id'];

    this.userService.getAllUser().subscribe({
      next: users => {
        this.allUsers.set(
          users.filter(user => user.id !== Number(localStorage.getItem("USER_ID")))
        );
        this.owner = users.filter(user => user.id === Number(localStorage.getItem("USER_ID")))[0];
      }
    })
    this.workspaceService.fetchBoards(this.workspaceId);
    this.getWorkspace();
    this.getBoards(this.workspaceId);
    this.mainService.setWorkspaceId(Number(this.workspaceId))

  }

  remove(user: User): void {
    this._users.update(users => users.filter(u => u.username !== user.username));
  }

  selected(event: MatAutocompleteSelectedEvent): void {
    const member: string = event.option.value;
    // // Vérifie qu'il n'est pas déjà sélectionné

   // const isDuplicate = this.tab.some(u => u === member);
   const isDuplicate = this._users().some(u => u.username === member)
     if (!isDuplicate) {


       const avatarUrl = this.allUsers().filter((user) => user.username === member)
       const existedUser = this.allUsers().filter((user) => user.username === member)
       existedUser[0].pictureUrl = avatarUrl[0].pictureUrl != null ? avatarUrl[0].pictureUrl : './circle-user.png';

       this._users.update(users => [...users, existedUser[0]]);

     }


    // Réinitialise le champ et l'autocomplete
    this.currentUser.setValue('');
    event.option.deselect();  // facultatif
  }

  public getWorkspace() {
    this.workspaceService.getWorkspaceById(this.workspaceId).subscribe({
      next: workspace => {
        this.mainService.setWorkspace(workspace.name)
        // this.workspaceName = workspace.name;
      }
    })

  }
  public getBoards(workspaceId: number) : void {

    this.workspaceService.getBoards(workspaceId).subscribe({
        next: (data: Board[]) => {
          //this.boardService.updateBoards(data);
          this.boards.set(data)
          this.userService.getUserById(Number(localStorage.getItem("USER_ID"))).subscribe({
            next: value => {
              console.log(value)
            }
          })
          this.boards.update(boardTab => boardTab.map((board) => {
            const updateAt = new Date(board.lastUpdated);
            const now = new Date();

            const diffWeeks = differenceInWeeks(now,updateAt);
            const diffDays = differenceInDays(now,updateAt);
            const diffHours = differenceInHours(now,updateAt);
            const diffMinutes = differenceInMinutes(now,updateAt);
            const diffSeconds = differenceInSeconds(now,updateAt);
            return {
              ...board,
              weeksSinceUpdate: diffWeeks,
              daysSinceUpdate: diffDays,
              hoursSinceUpdate: diffHours,
              minutesSinceUpdate: diffMinutes,
              secondsSinceUpdate: diffSeconds
            }
          }))
          this.mainService.setBoards(data);
        },
        error: (error: HttpErrorResponse) => {
          alert("error -> " + error.message)
        }
      }
    )
  }
  public filteredBoard() {
    return this.boards().filter((value) => {
      return this.searchTerm != '' ? value.name.includes(this.searchTerm) : value
    })
  }

  protected readonly localStorage = localStorage;
  protected readonly Number = Number;

  addBoard() {
    const userId = Number(localStorage.getItem("USER_ID"));
    const boardName = this.myForm.get('name');
    const desc = this.myForm.get('description')
    let newBoard : Board = {

      cardCount: 0,
      color: '',
      daysSinceUpdate: 0,
      hoursSinceUpdate: 0,
      lastUpdated: new Date(),
      members: [],
      minutesSinceUpdate: 0,
      ownerId: userId,
      secondsSinceUpdate: 0,
      weeksSinceUpdate: 0,
      name: boardName?.value,
      description: desc?.value

    }

      this.datePipe.transform(newBoard.lastUpdated, 'dd/MM/yyyy','fr')

      if(desc?.invalid && boardName?.invalid){
        alert("error : missing input")
      } else {

        newBoard.members = this.users();
        this.workspaceService.createBoard(this.workspaceId, newBoard).subscribe({
          next:boardValue => {
            boardValue.members.length = boardValue.members.length + 1
            this.boards.update((currentValue) => [...currentValue,boardValue]);

            this.workspaceService.setBoards(this.boards());

            this.currentUser.reset();

            this.cancel();
          }
        });

      }



  }

  cancel() {
    this.boardCreated = false;
    this.myForm.reset();
    this._users.set([]);
  }
}
