import { Component, EventEmitter, Input, Output } from '@angular/core';
import { NgClass, NgForOf, NgIf } from '@angular/common';
import { RouterLink } from '@angular/router';
import {IUser} from "../../../../core/models/IUser";

@Component({
  selector: 'app-plan-sidebar',
  standalone: true,
  imports: [NgIf, NgForOf, NgClass, RouterLink],
  templateUrl: './plan-sidebar.component.html',
  styleUrls: ['./plan-sidebar.component.scss']
})
export class PlanSidebarComponent {
  @Input() user: IUser | null = null;
  @Input() plans: any[] = [];
  @Input() selectedPlanId: string | null = null;
  @Input() loading = false;
  @Input() error = false;
  @Input() currentPage = 0;
  @Input() totalPages = 0;

  @Output() planSelect = new EventEmitter<any>();
  @Output() planDelete = new EventEmitter<string>();
  @Output() planCreate = new EventEmitter<void>();
  @Output() pageChange = new EventEmitter<number>();
  @Output() planEdit = new EventEmitter<any>();
  @Output() retry = new EventEmitter<void>();

  selectPlan(plan: any): void {
    this.planSelect.emit(plan);
  }

  deletePlan(event: Event, planId: string): void {
    event.stopPropagation();
    if (confirm('Are you sure you want to delete this plan?')) {
      this.planDelete.emit(planId);
    }
  }

  editPlan(event: Event, plan: any): void {
    event.stopPropagation();
    this.planEdit.emit(plan);
  }

  createNewPlan(): void {
    this.planCreate.emit();
  }

  onPageChange(page: number): void {
    this.pageChange.emit(page);
  }

  retryLoading(): void {
    this.retry.emit();
  }
}
