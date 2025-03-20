// planned-activity-form.component.ts with error handling
import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-planned-activity-form',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './planned-activity-form.component.html',
  styleUrls: ['./planned-activity-form.component.scss']
})
export class PlannedActivityFormComponent implements OnInit {
  @Input() activity: any;
  @Output() save = new EventEmitter<any>();
  @Output() cancel = new EventEmitter<void>();

  form: FormGroup;
  formSubmitted = false;
  errorMessage = '';
  isSubmitting = false;

  constructor(private fb: FormBuilder) {
    this.form = this.fb.group({
      priority: ['medium', [Validators.required]],
      startDate: [null, [Validators.required]],
      endDate: [null],
      notes: ['']
    });
  }

  ngOnInit() {
    if (this.activity) {
      // Convert priority from server (uppercase) to lowercase for form display
      const priority = this.activity.priority ?
        this.activity.priority.toLowerCase() : 'medium';

      this.form.patchValue({
        priority: priority,
        startDate: this.formatDateForInput(this.activity.startDate),
        endDate: this.formatDateForInput(this.activity.endDate),
        notes: this.activity.notes || ''
      });
    }
  }

  private formatDateForInput(dateString: string): string | null {
    if (!dateString) return null;

    try {
      const date = new Date(dateString);
      // Check if date is valid
      if (isNaN(date.getTime())) return null;

      // Format to YYYY-MM-DDThh:mm
      return date.toISOString().slice(0, 16);
    } catch (error) {
      console.error('Error formatting date', error);
      return null;
    }
  }

  onSubmit() {
    this.formSubmitted = true;
    this.errorMessage = '';

    if (this.form.valid) {
      this.isSubmitting = true;
      const formValues = this.form.value;

      // Validate end date is after start date if both are provided
      if (formValues.startDate && formValues.endDate) {
        const startDate = new Date(formValues.startDate);
        const endDate = new Date(formValues.endDate);

        if (endDate < startDate) {
          this.errorMessage = 'End date must be after start date';
          this.isSubmitting = false;
          return;
        }
      }

      const result = {
        priority: formValues.priority,
        startDate: formValues.startDate,
        endDate: formValues.endDate,
        notes: formValues.notes
      };

      this.save.emit(result);

      // Reset form state if error occurs (will be overridden by parent component closing the form on success)
      setTimeout(() => {
        this.isSubmitting = false;
      }, 500);
    } else {
      // Touch all form fields to trigger validation messages
      Object.keys(this.form.controls).forEach(key => {
        const control = this.form.get(key);
        control?.markAsTouched();
      });
    }
  }

  onCancel() {
    this.cancel.emit();
  }
}
