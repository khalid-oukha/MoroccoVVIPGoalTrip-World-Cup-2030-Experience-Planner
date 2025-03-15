import { Component, EventEmitter, Input, Output } from '@angular/core';
import { NgForOf, NgIf, NgClass, NgTemplateOutlet, DatePipe, CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-plan-detail',
  standalone: true,
  imports: [NgIf, NgForOf, RouterLink, NgClass, NgTemplateOutlet, DatePipe, CommonModule],
  templateUrl: './plan-detail.component.html',
  styleUrls: ['./plan-detail.component.scss']
})
export class PlanDetailComponent {
  @Input() plan: any | null = null;
  @Input() loading = false;

  @Output() activityRemove = new EventEmitter<{planId: string, activityId: string}>();
  @Output() createPlan = new EventEmitter<void>();
  @Output() editPlanEvent = new EventEmitter<any>();
  @Output() deletePlanEvent = new EventEmitter<string>();

  removeActivity(planId: string, activityId: string): void {
    if (confirm('Are you sure you want to remove this activity from the plan?')) {
      this.activityRemove.emit({planId, activityId});
    }
  }

  createNewPlan(): void {
    this.createPlan.emit();
  }

  editPlan(plan: any): void {
    this.editPlanEvent.emit(plan);
  }

  deletePlan(planId: string): void {
    if (confirm('Are you sure you want to delete this plan?')) {
      this.deletePlanEvent.emit(planId);
    }
  }

  getActivitiesByDate(activities: any[]): {[key: string]: any[]} {
    if (!activities || activities.length === 0) return {};

    const now = new Date();
    const today = new Date(now.getFullYear(), now.getMonth(), now.getDate());
    const groupedActivities: {[key: string]: any[]} = {};

    const sortedActivities = [...activities].sort((a, b) => {
      if (!a.startDate) return 1;
      if (!b.startDate) return -1;
      return new Date(a.startDate).getTime() - new Date(b.startDate).getTime();
    });

    for (const activity of sortedActivities) {
      if (!activity.startDate) continue;

      const activityDate = new Date(activity.startDate);
      const dateStr = new Date(activityDate.getFullYear(), activityDate.getMonth(), activityDate.getDate())
        .toISOString().split('T')[0];

      if (!groupedActivities[dateStr]) {
        groupedActivities[dateStr] = [];
      }

      activity.isPast = activityDate < today;
      activity.isToday =
        activityDate.getDate() === today.getDate() &&
        activityDate.getMonth() === today.getMonth() &&
        activityDate.getFullYear() === today.getFullYear();

      groupedActivities[dateStr].push(activity);
    }

    return groupedActivities;
  }

  getDayName(dateStr: unknown): string {
    if (typeof dateStr !== 'string') return '';
    const date = new Date(dateStr);
    return date.toLocaleDateString('en-US', { weekday: 'long' });
  }

  isDatePast(dateStr: unknown): boolean {
    if (typeof dateStr !== 'string') return false;
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    const date = new Date(dateStr);
    return date < today;
  }

  isDateToday(dateStr: unknown): boolean {
    if (typeof dateStr !== 'string') return false;
    const today = new Date();
    const date = new Date(dateStr);
    return date.getDate() === today.getDate() &&
      date.getMonth() === today.getMonth() &&
      date.getFullYear() === today.getFullYear();
  }

  formatDate(dateString: unknown): string {
    if (!dateString || typeof dateString !== 'string') return '';

    const date = new Date(dateString);
    return date.toLocaleDateString('en-US', {
      month: 'short',
      day: 'numeric'
    });
  }

  formatDateTime(dateString: string): string {
    if (!dateString) return '';

    const date = new Date(dateString);
    return date.toLocaleDateString('en-US', {
      month: 'short',
      day: 'numeric',
      year: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  }

  formatTime(dateString: string): string {
    if (!dateString) return '';

    const date = new Date(dateString);
    return date.toLocaleTimeString('en-US', {
      hour: '2-digit',
      minute: '2-digit'
    });
  }
}
