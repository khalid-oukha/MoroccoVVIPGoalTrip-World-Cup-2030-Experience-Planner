// src/app/modules/front-office/pages/activity-details/activity-details.component.ts
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { Activity } from '../../../../core/models/Activity';
import { ActivitesService } from '../../../../core/services/activites.service';
import { DatePipe, NgIf, NgClass } from '@angular/common';
import { AuthService } from '../../../../core/services/auth.service';

@Component({
  selector: 'app-activity-details',
  standalone: true,
  imports: [NgIf, DatePipe, NgClass, RouterLink],
  templateUrl: './activity-details.component.html',
  styleUrl: './activity-details.component.scss',
})
export class ActivityDetailsComponent implements OnInit {
  activity?: Activity;
  loading = true;
  error = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private activitiesService: ActivitesService,
    public authService: AuthService
  ) {}

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
}
