import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NgIf, NgOptimizedImage, NgFor } from '@angular/common';
import { AuthService } from '../../../../core/services/auth.service';
import { ActivitesService } from '../../../../core/services/activites.service';
import { Activity } from '../../../../core/models/Activity';
import { ActivityCardComponent } from '../../components/activity-card/activity-card.component';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [
    NgOptimizedImage,
    NgIf,
    NgFor,
    ActivityCardComponent,
  ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss',
})
export class HomeComponent implements OnInit {
  topActivities: Activity[] = [];
  loading = true;
  error = false;

  constructor(
    public authService: AuthService,
    private activitesService: ActivitesService,
    private router: Router
  ) {}

  ngOnInit() {
    this.loadTopActivities();
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

  onAddActivityToPlan(activityId: string) {
    if (this.authService.isLoggedIn()) {
      // TODO: Implement the actual plan integration
      console.log('Adding activity to plan:', activityId);
    } else {
      this.router.navigate(['/auth/login'], {
        queryParams: { returnUrl: `/activities/${activityId}` }
      });
    }
  }

  onViewActivityDetails(activityId: string) {
    this.router.navigate(['/planner/activities', activityId]);
  }

  navigateToActivities() {
    this.router.navigate(['planner/activities']);
  }
}
