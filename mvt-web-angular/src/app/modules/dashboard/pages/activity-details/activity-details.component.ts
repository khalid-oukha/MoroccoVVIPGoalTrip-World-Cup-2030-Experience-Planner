import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Activity } from '../../../../core/models/Activity';
import { ActivitesService } from '../../../../core/services/activites.service';
import { DatePipe, NgIf, NgOptimizedImage } from '@angular/common';

@Component({
  selector: 'app-activity-details',
  standalone: true,
  imports: [NgIf, DatePipe],
  templateUrl: './activity-details.component.html',
  styleUrl: './activity-details.component.scss',
})
export class ActivityDetailsComponent implements OnInit {
  activity?: Activity;

  constructor(
    private route: ActivatedRoute,
    private activitiesService: ActivitesService
  ) {}

  ngOnInit() {
    const activityId = this.route.snapshot.paramMap.get('id');
    if (activityId) {
      this.loadActivityDetails(activityId);
    }
  }

  loadActivityDetails(id: string) {
    this.activitiesService.findById(id).subscribe({
      next: (activity) => {
        this.activity = activity;
      },
      error: (err) => {
        console.error('Failed to load activity details:', err);
        this.activity = undefined;
      },
    });
  }
}
