import { Component, Input } from '@angular/core';
import { NgOptimizedImage } from '@angular/common';

@Component({
  selector: 'app-activity-card',
  standalone: true,
  imports: [NgOptimizedImage],
  templateUrl: './activity-card.component.html',
  styleUrl: './activity-card.component.scss',
})
export class ActivityCardComponent {
  @Input() imageUrl!: string;
  @Input() title!: string;
  @Input() description!: string;

  addToPlan() {
    // Handle add to plan logic here
    console.log('Added to plan:', this.title);
  }
}
