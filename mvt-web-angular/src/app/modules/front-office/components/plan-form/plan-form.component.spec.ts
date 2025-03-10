import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PlanFormComponent } from './plan-form.component';

describe('PlanFormComponent', () => {
  let component: PlanFormComponent;
  let fixture: ComponentFixture<PlanFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PlanFormComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PlanFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
