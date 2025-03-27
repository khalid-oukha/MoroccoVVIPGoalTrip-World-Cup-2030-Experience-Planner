import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { Activity } from '../../../../core/models/Activity';
import { ActivitesService } from '../../../../core/services/activites.service';
import { DatePipe, NgIf, NgClass } from '@angular/common';
import { AuthService } from '../../../../core/services/auth.service';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { PlanService } from '../../../../core/services/plan.service';
import { Plan } from '../../../../core/models/Plan';
import { finalize } from 'rxjs';

@Component({
  selector: 'app-activity-details',
  standalone: true,
  imports: [NgIf, DatePipe, NgClass, RouterLink, ReactiveFormsModule],
  templateUrl: './activity-details.component.html',
  styleUrl: './activity-details.component.scss',
})
export class ActivityDetailsComponent implements OnInit {
  activity?: Activity;
  loading = true;
  error = false;

  // Add to Plan properties
  showModal = false;
  formLoading = false;
  userPlans: Plan[] = [];
  planForm: FormGroup;
  formError = '';
  formSuccess = false;

  // Toast notification
  showActivityAddedToast = false;
  activityAddedMessage = '';

  priorities = [
    { value: 'LOW', label: 'Low' },
    { value: 'MEDIUM', label: 'Medium' },
    { value: 'HIGH', label: 'High' }
  ];

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private activitiesService: ActivitesService,
    private planService: PlanService,
    private fb: FormBuilder,
    public authService: AuthService
  ) {
    this.planForm = this.fb.group({
      planId: ['', Validators.required],
      priority: ['MEDIUM', Validators.required],
      startDate: ['', Validators.required],
      endDate: ['', Validators.required],
      notes: ['']
    });
  }

  ngOnInit() {
    const activityId = this.route.snapshot.paramMap.get('id');
    if (activityId) {
      this.loadActivityDetails(activityId);
    } else {
      this.router.navigate(['/activities']);
    }
  }

  loadActivityDetails(id: string) {
    this.loading = true;
    this.error = false;

    this.activitiesService.findById(id).subscribe({
      next: (activity) => {
        this.activity = activity;
        this.loading = false;
      },
      error: (err) => {
        console.error('Failed to load activity details:', err);
        this.loading = false;
        this.error = true;
      },
    });
  }

  addToPlan() {
    if (this.activity && this.activity.available) {
      this.showModal = true;
      this.formSuccess = false;
      this.formError = '';
      this.loadUserPlans();
    }
  }

  closeModal(event?: Event) {
    if (event) event.stopPropagation();
    this.showModal = false;
  }

  loadUserPlans() {
    this.formLoading = true;
    this.planService.getMyPlans()
      .pipe(finalize(() => this.formLoading = false))
      .subscribe({
        next: (response) => {
          this.userPlans = response.content;
          if (this.userPlans.length > 0) {
            this.planForm.get('planId')?.setValue(this.userPlans[0].id);
          }
        },
        error: (err) => {
          console.error('Error loading plans:', err);
          this.formError = 'Failed to load your plans. Please try again.';
        }
      });
  }

  onSubmit(event: Event) {
    event.preventDefault();
    if (this.planForm.invalid || !this.activity) return;

    const formData = this.planForm.value;
    this.formLoading = true;
    this.formError = '';

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

    this.planService.addActivityToPlan(formData.planId, this.activity.id, requestData)
      .pipe(finalize(() => this.formLoading = false))
      .subscribe({
        next: () => {
          this.formSuccess = true;

          // Show toast notification
          this.showActivityAddedToast = true;
          this.activityAddedMessage = `${this.activity?.name} added to plan successfully!`;

          // Hide toast after 3 seconds
          setTimeout(() => {
            this.showActivityAddedToast = false;
          }, 3000);

          // Auto close modal after success
          setTimeout(() => {
            this.closeModal();
          }, 2000);
        },
        error: (err) => {
          console.error('Error adding to plan:', err);
          this.formError = 'Failed to add activity to plan. Please try again.';
        }
      });
  }
}
