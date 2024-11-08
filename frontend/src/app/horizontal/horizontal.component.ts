import {Component} from '@angular/core';
import {CdkDragDrop, CdkDrag, CdkDropList, moveItemInArray} from '@angular/cdk/drag-drop';

@Component({
  selector: 'app-horizontal-drag-drop',
  templateUrl: 'horizontal.component.html',
  styleUrl: 'horizontal.component.css',
  standalone: true,
  imports: [CdkDropList, CdkDrag],
})
export class HorizontalDragDropExampleComponent {
  timePeriods = [
    'Bronze age',
    'Iron age',
    'Middle ages',
    'Early modern period',
    'Long nineteenth century',
  ];

  drop(event: CdkDragDrop<string[]>) {
    moveItemInArray(this.timePeriods, event.previousIndex, event.currentIndex);
  }
}
