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
  workspaceName: string = "";

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

  toggleFavorite() {
    this.onFavoriteToggle.emit(this.workspace.id);
  }
}
