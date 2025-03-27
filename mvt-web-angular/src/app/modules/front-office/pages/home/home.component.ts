import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NgIf, NgOptimizedImage, NgFor } from '@angular/common';
import { AuthService } from '../../../../core/services/auth.service';
import { ActivitesService } from '../../../../core/services/activites.service';
import { Activity } from '../../../../core/models/Activity';
import { ActivityCardComponent } from '../../components/activity-card/activity-card.component';
import { CategoryCardComponent } from "../../components/category-card/category-card.component";
import { Category } from "../../../../core/models/Category";
import { CategoriesService } from "../../../../core/services/categories.service";

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [
    NgOptimizedImage,
    NgIf,
    NgFor,
    ActivityCardComponent,
    CategoryCardComponent
  ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss',
})
export class HomeComponent implements OnInit {
  topActivities: Activity[] = [];
  categories: Category[] = [];
  loading = true;
  error = false;

  showActivityAddedToast = false;
  activityAddedMessage = '';

  constructor(
    public authService: AuthService,
    private activitesService: ActivitesService,
    private categoriesService: CategoriesService,
    private router: Router
  ) {}

  ngOnInit() {
    this.loadTopActivities();
    this.loadCategories();
  }

  loadTopActivities() {
    this.loading = true;
    this.error = false;
    this.activitesService.getTopActivities(4).subscribe({
      next: (activities) => {
        this.topActivities = activities;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading top activities:', error);
        this.loading = false;
        this.error = true;
      }
    });
  }

  private loadCategories() {
    this.categoriesService.findAll(0, 8, 'name,asc').subscribe({
      next: (categories) => this.categories = categories,
      error: (error) => console.error('Error loading categories:', error)
    });
  }

  onViewActivityDetails(activityId: string) {
    this.router.navigate(['/planner/activities', activityId]);
  }

  navigateToActivities() {
    this.router.navigate(['planner/activities']);
  }

  /**
   * Handle activity add to plan success
   * This is triggered from the ActivityCardComponent
   */
  onActivityAddedToPlan(event: {planId: string, activityId: string}) {
    // Find the activity name for the toast message
    const activity = this.topActivities.find(a => a.id === event.activityId);
    const activityName = activity ? activity.name : 'Activity';

    // Show success toast
    this.showActivityAddedToast = true;
    this.activityAddedMessage = `${activityName} added to plan successfully!`;

    // Hide toast after 3 seconds
    setTimeout(() => {
      this.showActivityAddedToast = false;
    }, 3000);
  }
}
