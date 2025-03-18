// planned-activity-form.component.ts
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-planned-activity-form',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './planned-activity-form.component.html',
  styleUrls: ['./planned-activity-form.component.scss']
})
export class PlannedActivityFormComponent {
  @Input() activity: any;
  @Output() save = new EventEmitter<any>();
  @Output() cancel = new EventEmitter<void>();

  form: FormGroup;

  constructor(private fb: FormBuilder) {
    this.form = this.fb.group({
      priority: ['medium', [Validators.required]],
      startDate: [null, [Validators.required]],
      endDate: [null]
    });
  }

  ngOnInit() {
    if (this.activity) {
      this.form.patchValue({
        priority: this.activity.priority || 'medium',
        startDate: this.formatDateForInput(this.activity.startDate),
        endDate: this.formatDateForInput(this.activity.endDate)
      });
    }
  }

  private formatDateForInput(dateString: string): string | null {
    return dateString ? new Date(dateString).toISOString().slice(0, 16) : null;
  }

  onSubmit() {
    if (this.form.valid) {
      this.save.emit(this.form.value);
    }
  }

  onCancel() {
    this.cancel.emit();
  }
}
