// activity-card.component.ts
import {Component, Input, Output, EventEmitter, booleanAttribute} from '@angular/core';
import { NgIf, NgOptimizedImage } from '@angular/common';

@Component({
  selector: 'app-activity-card',
  standalone: true,
  imports: [NgOptimizedImage, NgIf],
  templateUrl: './activity-card.component.html',
  styleUrl: './activity-card.component.scss',
})
export class ActivityCardComponent {
  @Input() imageUrl!: string;
  @Input() title!: string;
  @Input() description!: string;
  @Input() category?: string;
  @Input() location?: string;
  @Input() available: boolean = true;
  @Input({transform: booleanAttribute}) showLocation: boolean = false;
  @Input() activityId?: string;

  @Output() addToPlanEvent = new EventEmitter<string>();
  @Output() viewDetailsEvent = new EventEmitter<string>();

  addToPlan() {
    if (this.available) {
      this.addToPlanEvent.emit(this.activityId || this.title);
    }
  }

  viewDetails() {
    this.viewDetailsEvent.emit(this.activityId || this.title);
  }
}
