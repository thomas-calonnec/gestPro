import {Component, EventEmitter, inject, OnInit, Output, signal, WritableSignal} from '@angular/core';
import {Workspace} from '@models/workspace';
import {UserService} from '@services/users/user.service';
import {ActivatedRoute, RouterLink} from '@angular/router';
import {MainService} from '@services/main/main.service';
import {MatButton} from '@angular/material/button';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';

@Component({
  selector: 'app-user',
  standalone: true,

  templateUrl:'user.component.html',
  imports: [
    RouterLink,
    MatButton,
    ReactiveFormsModule
  ],
  styleUrl: './user.component.css'
})
export class UserComponent implements  OnInit{
  workspaces : WritableSignal<Workspace[]> = signal([]);
  userId: string = "";
  userService : UserService = inject(UserService);
  route : ActivatedRoute = inject(ActivatedRoute);
  mainService : MainService = inject(MainService);
  @Output() paramId  = new EventEmitter<string>();
  workspaceCreated: boolean = false;
  private formBuilder : FormBuilder = inject(FormBuilder);
  myForm: FormGroup;

  constructor() {
    this.myForm = this.formBuilder.group({
      name: ['', Validators.required]
    })
  }
  ngOnInit(): void{
    this.userId = this.route.snapshot.params['userId'];
    this.paramId.emit(this.userId);
    this.getWorkspaces();
    localStorage.removeItem("workspaceId")
    this.mainService.removeListBoard()

  }

  getWorkspaces(): void {

    this.userService.getWorkspaces(this.userId).subscribe({
      next: (workspaces: Workspace[]) => {
        this.workspaces.set(workspaces);
      },
      error: err => {console.error(err)}
    })
  }


  addWorkspace() {
   const workspace:Workspace =  {
     id: 0,
     name: this.myForm.value.name
   };
   this.userService.createWorkspace(Number(this.userId),workspace).subscribe({
     next: data => {
       this.workspaceCreated = false;
      this.workspaces.update(currentValue => [...currentValue,data]);

     }
   })
  }
}
