import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PlanSidebarComponent } from './plan-sidebar.component';

describe('PlanSidebarComponent', () => {
  let component: PlanSidebarComponent;
  let fixture: ComponentFixture<PlanSidebarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PlanSidebarComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PlanSidebarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
