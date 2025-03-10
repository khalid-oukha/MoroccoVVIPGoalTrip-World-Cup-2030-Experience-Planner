import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddToPlanModalComponent } from './add-to-plan-modal.component';

describe('AddToPlanModalComponent', () => {
  let component: AddToPlanModalComponent;
  let fixture: ComponentFixture<AddToPlanModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AddToPlanModalComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AddToPlanModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
