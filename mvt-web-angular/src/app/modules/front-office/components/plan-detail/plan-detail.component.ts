import { Component, EventEmitter, Input, Output } from '@angular/core';
import { NgForOf, NgIf } from '@angular/common';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-plan-detail',
  standalone: true,
  imports: [NgIf, NgForOf, RouterLink],
  templateUrl: './plan-detail.component.html',
  styleUrls: ['./plan-detail.component.scss']
})
export class PlanDetailComponent {
  @Input() plan: any | null = null;
  @Input() loading = false;

  @Output() activityRemove = new EventEmitter<{planId: string, activityId: string}>();
  @Output() createPlan = new EventEmitter<void>();

  removeActivity(planId: string, activityId: string): void {
    if (confirm('Are you sure you want to remove this activity from the plan?')) {
      this.activityRemove.emit({planId, activityId});
    }
  }

  createNewPlan(): void {
    this.createPlan.emit();
  }
}
