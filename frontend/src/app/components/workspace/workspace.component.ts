import {
  Component,
   EventEmitter,
  inject,
  Input,
  OnInit,
  Output,

} from '@angular/core';
import {WorkspaceService} from '@services/workspaces/workspace.service';
import {RouterLink} from '@angular/router';
import {MainService} from '@services/main/main.service';
import {FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import {MatButtonModule} from '@angular/material/button';
import { registerLocaleData} from '@angular/common';
import localeFr from '@angular/common/locales/fr';

import {MatChipsModule} from '@angular/material/chips';
import { MatIconModule } from '@angular/material/icon';
import {MatAutocompleteModule} from '@angular/material/autocomplete';
import { MatFormFieldModule } from '@angular/material/form-field';
import {MatCardModule} from '@angular/material/card';
import {Workspace} from '@models/workspace';
import {InviteUserDialogComponent} from '@components/invite-user-dialog/invite-user-dialog.component';
import {MatDialog} from '@angular/material/dialog';
import {InvitationService} from '@services/invitations/invitation.service';
import {MatSnackBar} from '@angular/material/snack-bar';
import {Invitation} from '@models/invitation';

registerLocaleData(localeFr);

@Component({
    selector: 'app-workspace',
    templateUrl: 'workspace.component.html',
    providers: [
    ],
  imports: [
    MatFormFieldModule, MatChipsModule, MatIconModule, MatAutocompleteModule, FormsModule,
    RouterLink,
    ReactiveFormsModule,
    MatButtonModule,
    FormsModule,
     MatCardModule

  ],
    styleUrl: './workspace.component.css'
})
export class WorkspaceComponent implements OnInit{

  private workspaceId : number = 0;
  private workspaceService: WorkspaceService = inject(WorkspaceService);
   mainService: MainService = inject(MainService);
  private dialog: MatDialog = inject(MatDialog);
  private snackBar: MatSnackBar = inject(MatSnackBar);
  private invitationService: InvitationService = inject(InvitationService);
  workspaceName: string = "";
  invitation : Invitation = {
    email : "",
    id: 0,
    status: "PENDING",
    createdAt: new Date()
  }

  myForm: FormGroup;

  @Input() workspace!: Workspace;
  @Output() onFavoriteToggle = new EventEmitter<number>();

  constructor() {
    this.myForm = new FormGroup({
      name: new FormControl('',[Validators.required]),
      description: new FormControl('',[Validators.required]),
      selectedItems: new FormControl([]) // chips choisies
    });

  }

  ngOnInit(): void {
    this.getWorkspace();
    this.mainService.setWorkspaceId(Number(this.workspaceId))

  }

  public getWorkspace() {

    this.workspaceService.getWorkspaceById(this.workspace.id).subscribe({
      next: workspace => {
        this.mainService.setWorkspace(workspace.name)
        // this.workspaceName = workspace.name;
      },
      error: err => {
        console.log("error")
        console.error(err)
      }
    })

  }
  selectedWorkspaceForInvite: Workspace | null = null;
  inviteEmail: string = '';

  openInvitePopup(workspace: Workspace): void {
    this.selectedWorkspaceForInvite = workspace;
  }

  closeInvitePopup(): void {
    this.selectedWorkspaceForInvite = null;
    this.inviteEmail = '';
  }

  sendInvite(): void {
    if (!this.inviteEmail.includes('@')) {
      alert('Invalid email');
      return;
    }

   // this.invitation = {
   //   createdAt: new Date(),
   //   id: 0,
   //   invitee: 0,
   //   email: this.inviteEmail,
   //    workspace: this.selectedWorkspaceForInvite!,
   //    status: "PENDING",
   //    invitedBy: 0
   // }
   //
   //  console.log(this.invitation)
   //  this.invitationService.inviteUserToWorkspace(this.invitation).subscribe({
   //   next: (value) => {
   //     console.log("invitation created ! ", value)
   //   },
   //   error : (err) => {
   //     console.error("error : ",err);
   //   }
   // })
    console.log(`Inviting ${this.inviteEmail} to ${this.selectedWorkspaceForInvite?.name}`);
    this.closeInvitePopup();
  }

  openInviteDialog(workspace : Workspace): void {
    const dialogRef = this.dialog.open(InviteUserDialogComponent, {
      width: '400px',
      data: { workspaceName: workspace.name }
    });


    dialogRef.afterClosed().subscribe(email => {

      if (email) {
        this.invitation = {
          createdAt: new Date(),
          id: 0,
          email: email,
          workspace: workspace,
          status: "PENDING",
        }

        this.invitationService.inviteUserToWorkspace(this.invitation).subscribe({
          next: () => {
            this.snackBar.open('Invitation sent!', 'Close', { duration: 3000 });
          },
          error: (err) => {
            console.log(err)
            this.snackBar.open('Failed to send invitation.', 'Close', { duration: 3000 });
          }
        });
      }
    });
  }
  toggleFavorite() {
    this.onFavoriteToggle.emit(this.workspace.id);
  }
}
