import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogAnimationsExampleDialogComponent } from './dialog-animations-example-dialog.component';

describe('DialogAnimationsExampleDialogComponent', () => {
  let component: DialogAnimationsExampleDialogComponent;
  let fixture: ComponentFixture<DialogAnimationsExampleDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DialogAnimationsExampleDialogComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DialogAnimationsExampleDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
