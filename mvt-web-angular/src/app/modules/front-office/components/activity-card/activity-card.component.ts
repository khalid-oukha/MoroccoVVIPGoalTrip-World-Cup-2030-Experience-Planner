import { booleanAttribute, Component, ElementRef, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { PlanService } from '../../../../core/services/plan.service';
import { finalize } from 'rxjs';
import { Plan } from '../../../../core/models/Plan';

@Component({
  selector: 'app-activity-card',
  standalone: true,
  imports: [CommonModule, RouterLink, ReactiveFormsModule],
  templateUrl: './activity-card.component.html',
  styleUrls: ['./activity-card.component.scss']
})
export class ActivityCardComponent {
  @Input() imageUrl: string = '';
  @Input() title: string = '';
  @Input() description: string = '';
  @Input() category: string = '';
  @Input() location: string = '';
  @Input() available: boolean = true;
  @Input() activityId: string = '';
  @Input({ transform: booleanAttribute }) showLocation: boolean = false;

  @Output() viewDetailsEvent = new EventEmitter<string>();
  @Output() addToPlanSuccessEvent = new EventEmitter<{planId: string, activityId: string}>();

  // Modal states
  showModal = false;
  loading = false;
  userPlans: Plan[] = [];
  planForm: FormGroup;
  error = '';
  success = false;

  priorities = [
    { value: 'LOW', label: 'Low' },
    { value: 'MEDIUM', label: 'Medium' },
    { value: 'HIGH', label: 'High' }
  ];

  constructor(
    private planService: PlanService,
    private fb: FormBuilder
  ) {
    this.planForm = this.fb.group({
      planId: ['', Validators.required],
      priority: ['MEDIUM', Validators.required],
      startDate: ['', Validators.required],
      endDate: ['', Validators.required],
      notes: ['']
    });
  }

  viewDetails(): void {
    this.viewDetailsEvent.emit(this.activityId);
  }

  addToPlan(event: Event): void {
    event.stopPropagation();
    if (!this.available) return;

    this.showModal = true;
    this.success = false;
    this.error = '';
    this.loadUserPlans();
  }

  closeModal(event?: Event): void {
    if (event) event.stopPropagation();
    this.showModal = false;
  }

  loadUserPlans(): void {
    this.loading = true;
    this.planService.getMyPlans()
      .pipe(finalize(() => this.loading = false))
      .subscribe({
        next: (response) => {
          this.userPlans = response.content;
          if (this.userPlans.length > 0) {
            this.planForm.get('planId')?.setValue(this.userPlans[0].id);
          }
        },
        error: (err) => {
          console.error('Error loading plans:', err);
          this.error = 'Failed to load your plans. Please try again.';
        }
      });
  }

  onSubmit(event: Event): void {
    event.stopPropagation();
    if (this.planForm.invalid) return;

    const formData = this.planForm.value;
    this.loading = true;
    this.error = '';

    // Format dates to ISO strings if they aren't already
    const startDate = formData.startDate instanceof Date ?
      formData.startDate.toISOString() : formData.startDate;
    const endDate = formData.endDate instanceof Date ?
      formData.endDate.toISOString() : formData.endDate;

    const requestData = {
      priority: formData.priority,
      startDate: startDate,
      endDate: endDate,
      notes: formData.notes
    };

    this.planService.addActivityToPlan(formData.planId, this.activityId, requestData)
      .pipe(finalize(() => this.loading = false))
      .subscribe({
        next: () => {
          this.success = true;
          this.addToPlanSuccessEvent.emit({
            planId: formData.planId,
            activityId: this.activityId
          });

          // Auto close after success
          setTimeout(() => {
            this.closeModal();
          }, 2000);
        },
        error: (err) => {
          console.error('Error adding to plan:', err);
          this.error = 'Failed to add activity to plan. Please try again.';
        }
      });
  }
}
