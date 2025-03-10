// src/app/components/activities/plan-selection-dropdown/plan-selection-dropdown.component.ts
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { finalize } from 'rxjs';
import {PlanService} from "../../../../core/services/plan.service";
import {ReactiveFormsModule} from "@angular/forms";

@Component({
  selector: 'app-plan-selection-dropdown',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './plan-selection-dropdown.component.html',
  styleUrls: ['./plan-selection-dropdown.component.scss']
})
export class PlanSelectionDropdownComponent implements OnInit {
  @Input() activityId: string = '';
  @Input() triggerRef: HTMLElement | null = null;
  @Output() close = new EventEmitter<void>();
  @Output() planSelected = new EventEmitter<{planId: string, planName: string}>();

  plans: any[] = [];
  loading = false;
  error = false;
  isVisible = false;
  dropdownPosition = { top: '0', left: '0' };

  constructor(private planService: PlanService) {}

  ngOnInit(): void {
    this.loadPlans();
    this.positionDropdown();

    // Add click outside listener
    document.addEventListener('click', this.handleClickOutside.bind(this));
  }

  ngOnDestroy(): void {
    document.removeEventListener('click', this.handleClickOutside.bind(this));
  }

  loadPlans(): void {
    this.loading = true;
    this.error = false;

    this.planService.getMyPlans()
      .pipe(finalize(() => this.loading = false))
      .subscribe({
        next: (response) => {
          this.plans = response.content || [];
        },
        error: (err) => {
          console.error('Error loading plans', err);
          this.error = true;
        }
      });
  }

  positionDropdown(): void {
    if (this.triggerRef) {
      const rect = this.triggerRef.getBoundingClientRect();
      this.dropdownPosition = {
        top: `${rect.bottom + window.scrollY + 10}px`,
        left: `${rect.left + window.scrollX}px`
      };
    }
  }

  handleClickOutside(event: MouseEvent): void {
    if (!this.triggerRef?.contains(event.target as Node) &&
      !(event.target as HTMLElement).closest('.plan-dropdown')) {
      this.close.emit();
    }
  }

  onPlanSelect(planId: string, planName: string): void {
    this.planSelected.emit({ planId, planName });
  }

  createNewPlan(): void {
    // Navigate to plan creation page
    window.location.href = '/my-plans/create';
  }
}
