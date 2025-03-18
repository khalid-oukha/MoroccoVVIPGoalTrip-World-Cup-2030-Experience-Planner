import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PlannedActivityFormComponent } from './planned-activity-form.component';

describe('PlannedActivityFormComponent', () => {
  let component: PlannedActivityFormComponent;
  let fixture: ComponentFixture<PlannedActivityFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PlannedActivityFormComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PlannedActivityFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
