import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HorizontalDragDropExampleComponent } from './horizontal.component';

describe('HorizontalComponent', () => {
  let component: HorizontalDragDropExampleComponent;
  let fixture: ComponentFixture<HorizontalDragDropExampleComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HorizontalDragDropExampleComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(HorizontalDragDropExampleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
