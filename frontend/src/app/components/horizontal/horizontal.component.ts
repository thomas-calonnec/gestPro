import {Component, computed, signal} from '@angular/core';
import {CdkDragDrop, CdkDrag, CdkDropList, moveItemInArray} from '@angular/cdk/drag-drop';
import {MatProgressBar} from '@angular/material/progress-bar';
import {MatCheckbox} from '@angular/material/checkbox';
import {FormsModule} from '@angular/forms';
import {NgForOf} from '@angular/common';

@Component({
  selector: 'app-horizontal-drag-drop',
  templateUrl: 'horizontal.component.html',
  styleUrl: 'horizontal.component.css',
  standalone: true,
  imports: [CdkDropList, CdkDrag, MatProgressBar, MatCheckbox, FormsModule, NgForOf],
})
export class HorizontalDragDropExampleComponent {
  // Liste des tâches
  tasks = [
    { name: 'Tâche 1', completed: false },
    { name: 'Tâche 2', completed: false },
    { name: 'Tâche 3', completed: false },
    { name: 'Tâche 4', completed: false },
  ];

  // Propriétés pour la progression
  progress: number = 0;

  // Calcul du nombre de tâches terminées
  get completedTasksCount(): number {
    return this.tasks.filter(task => task.completed).length;
  }

  // Total des tâches
  get totalTasksCount(): number {
    return this.tasks.length;
  }

  // Mise à jour de la progression
  updateProgress(): void {
    this.progress = (this.completedTasksCount / this.totalTasksCount) * 100;
  }
}
