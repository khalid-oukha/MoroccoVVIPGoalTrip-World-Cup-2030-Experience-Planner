import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PlanSelectionDropdownComponent } from './plan-selection-dropdown.component';

describe('PlanSelectionDropdownComponent', () => {
  let component: PlanSelectionDropdownComponent;
  let fixture: ComponentFixture<PlanSelectionDropdownComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PlanSelectionDropdownComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PlanSelectionDropdownComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
