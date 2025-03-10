import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';

@Component({
  selector: 'app-add-to-plan-modal',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './add-to-plan-modal.component.html',
  styleUrls: ['./add-to-plan-modal.component.scss']
})
export class AddToPlanModalComponent implements OnInit {
  @Input() activityName: string = '';
  @Input() planName: string = '';
  @Input() planId: string = '';
  @Input() activityId: string = '';

  // States for the modal
  @Input() isSubmitting = false;
  @Input() hasError = false;
  @Input() isSuccess = false;
  @Input() successMessage = '';

  @Output() close = new EventEmitter<void>();
  @Output() save = new EventEmitter<any>();

  form: FormGroup;
  priorities = [
    { value: 'LOW', label: 'Low Priority' },
    { value: 'MEDIUM', label: 'Medium Priority' },
    { value: 'HIGH', label: 'High Priority' }
  ];

  constructor(private fb: FormBuilder) {
    this.form = this.fb.group({
      priority: ['MEDIUM', Validators.required],
      startDate: [this.getCurrentDateTime(), Validators.required],
      endDate: [this.getCurrentDateTime(2), Validators.required],
      notes: ['']
    });
  }

  ngOnInit(): void {
    // Initialize form with default values
    this.form = this.fb.group({
      priority: ['MEDIUM', Validators.required],
      startDate: [this.getCurrentDateTime(), Validators.required],
      endDate: [this.getCurrentDateTime(2), Validators.required],
      notes: ['']
    });
  }
  getCurrentDateTime(hoursToAdd: number = 0): string {
    const date = new Date();
    date.setHours(date.getHours() + hoursToAdd);
    return this.formatDateForInput(date);
  }
  formatDateForInput(date: Date): string {
    return date.toISOString().slice(0, 16);
  }

  onSubmit(): void {
    if (this.form.valid) {
      // Format dates for API
      const formData = { ...this.form.value };
      formData.startDate = new Date(formData.startDate).toISOString();
      formData.endDate = new Date(formData.endDate).toISOString();

      this.save.emit({
        planId: this.planId,
        activityId: this.activityId,
        data: formData
      });
    }
  }

  closeModal(): void {
    this.close.emit();
  }
}
