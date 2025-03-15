import { Component, OnInit } from '@angular/core';
import { CommonModule, NgClass, NgForOf, NgIf, NgOptimizedImage } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ActivatedRoute, Router } from '@angular/router';
import { finalize } from 'rxjs';
import { IUser } from "../../../../core/models/IUser";
import { PlanService } from "../../../../core/services/plan.service";
import { AuthService } from "../../../../core/services/auth.service";
import { PlanFormComponent } from "../../components/plan-form/plan-form.component";
import { PlanDetailComponent } from "../../components/plan-detail/plan-detail.component";
import { PlanSidebarComponent } from "../../components/plan-sidebar/plan-sidebar.component";

@Component({
  selector: 'app-my-plans',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    PlanSidebarComponent,
    PlanDetailComponent,
    PlanFormComponent,
    NgIf,
    NgClass,
    NgOptimizedImage
  ],
  templateUrl: './my-plans.component.html',
  styleUrls: ['./my-plans.component.scss']
})
export class MyPlansComponent implements OnInit {
  plans: any[] = [];
  user: IUser | null = null;
  selectedPlan: any | null = null;
  loading = true;
  loadingPlanDetails = false;
  error = false;
  currentPage = 0;
  pageSize = 10;
  totalPlans = 0;
  totalPages = 0;

  showForm = false;
  editingPlan: any = null;
  isEditMode = false;

  constructor(
    private planService: PlanService,
    private authService: AuthService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.user = this.authService.getCurrentUser();
    this.authService.userDetails$.subscribe(user => {
      this.user = user;
    });

    this.route.paramMap.subscribe(params => {
      const planId = params.get('planId');
      if (planId) {
        this.loadPlanDetails(planId);
      }
    });

    this.loadUserPlans();
  }

  loadUserPlans(): void {
    this.loading = true;
    this.error = false;

    this.planService.getMyPlans(this.currentPage, this.pageSize)
      .pipe(finalize(() => this.loading = false))
      .subscribe({
        next: (response) => {
          this.plans = response.content;
          this.totalPlans = response.totalElements;
          this.totalPages = response.totalPages;

          if (!this.selectedPlan && this.plans.length > 0 && !this.route.snapshot.paramMap.get('planId')) {
            this.loadPlanDetails(this.plans[0].id);
          }
        },
        error: (err) => {
          console.error('Error loading plans', err);
          this.error = true;
        }
      });
  }

  loadPlanDetails(planId: string): void {
    this.loadingPlanDetails = true;
    this.planService.getPlanById(planId)
      .pipe(finalize(() => this.loadingPlanDetails = false))
      .subscribe({
        next: (plan) => {
          this.selectedPlan = plan;
        },
        error: (err) => {
          console.error('Error loading plan details', err);
        }
      });
  }

  onSelectPlan(plan: any): void {
    if (this.selectedPlan?.id !== plan.id) {
      this.loadPlanDetails(plan.id);
      this.closeForm();
    }
  }

  onPageChange(page: number): void {
    this.currentPage = page;
    this.loadUserPlans();
  }

  onCreatePlan(): void {
    this.showForm = true;
    this.isEditMode = false;
    this.editingPlan = null;
  }

  onEditPlan(plan: any): void {
    this.showForm = true;
    this.isEditMode = true;
    this.editingPlan = plan;
  }

  closeForm(): void {
    this.showForm = false;
    this.isEditMode = false;
    this.editingPlan = null;
  }

  onSavePlan(data: any): void {
    if (this.isEditMode && this.editingPlan) {
      const formData = data instanceof FormData ? data : data.data;
      const planId = data instanceof FormData ? this.editingPlan.id : data.id;

      this.planService.updatePlan(planId, formData).subscribe({
        next: (updatedPlan) => {
          const index = this.plans.findIndex(p => p.id === updatedPlan.id);
          if (index !== -1) {
            this.plans[index] = updatedPlan;
          }

          if (this.selectedPlan?.id === updatedPlan.id) {
            this.selectedPlan = updatedPlan;
          }

          this.closeForm();
        },
        error: () => {}
      });
    } else {
      this.planService.createPlan(data).subscribe({
        next: (newPlan) => {
          this.plans.unshift(newPlan);
          this.loadPlanDetails(newPlan.id);
          this.closeForm();
        },
        error: () => {}
      });
    }
  }

  onDeletePlan(planId: string): void {
    this.planService.deletePlan(planId).subscribe({
      next: () => {
        this.plans = this.plans.filter(p => p.id !== planId);
        if (this.selectedPlan?.id === planId) {
          this.selectedPlan = this.plans.length > 0 ? this.plans[0] : null;
          if (this.selectedPlan) {
            this.loadPlanDetails(this.selectedPlan.id);
          } else {
            this.router.navigate(['my-plans']);
          }
        }
      },
      error: (err) => {
        console.error('Error deleting plan', err);
      }
    });
  }

  onRemoveActivity(data: {planId: string, activityId: string}): void {
    this.planService.removeActivityFromPlan(data.planId, data.activityId).subscribe({
      next: () => {
        this.loadPlanDetails(data.planId);
      },
      error: () => {
        // Silent error handling or display a message if needed
      }
    });
  }
}
