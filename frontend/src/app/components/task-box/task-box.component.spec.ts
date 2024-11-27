import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TaskBoxComponent } from './task-box.component';

describe('TaskBoxComponent', () => {
  let component: TaskBoxComponent;
  let fixture: ComponentFixture<TaskBoxComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TaskBoxComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TaskBoxComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
