// form.component.ts
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivitesService } from '../../../../core/services/activites.service';
import { Category } from '../../../../core/models/Category';
import { City } from '../../../../core/models/City';
import { NgForOf, NgIf, NgOptimizedImage } from '@angular/common';
import { Activity } from '../../../../core/models/Activity';

@Component({
  selector: 'app-form',
  standalone: true,
  imports: [ReactiveFormsModule, NgForOf, NgIf, NgOptimizedImage],
  templateUrl: './form.component.html',
  styleUrl: './form.component.scss',
})
export class FormComponent implements OnInit {
  @Input() activityId?: string;
  @Input() categories: Category[] = [];
  @Input() cities: City[] = [];
  @Input() initialData?: Activity;

  @Output() saved = new EventEmitter<void>();
  @Output() cancelled = new EventEmitter<void>();

  form: FormGroup;
  selectedFile?: File;
  imagePreview?: string;

  constructor(private fb: FormBuilder, private activitesService: ActivitesService) {
    this.form = this.fb.group({
      name: ['', Validators.required],
      description: ['', Validators.required],
      location: ['', Validators.required],
      categoryId: [null, Validators.required],
      cityId: [null, Validators.required],
      available: [true],
      imageUri: [null],
    });
  }

  ngOnInit() {
    if (this.activityId) {
      this.activitesService.findById(this.activityId).subscribe((activity) => {
        this.form.patchValue({
          name: activity.name,
          description: activity.description,
          location: activity.location,
          categoryId: activity.category.id,
          cityId: activity.city.id,
          available: activity.available,
        });
        this.imagePreview = activity.imageUri;
      });
    }
  }

  onFileSelected(event: any) {
    const file = event.target.files[0];
    if (file) {
      this.selectedFile = file;
      this.form.patchValue({ imageUri: file });
      this.form.get('imageUri')?.updateValueAndValidity();

      const reader = new FileReader();
      reader.onload = () => (this.imagePreview = reader.result as string);
      reader.readAsDataURL(file);
    }
  }

  onSubmit() {
    if (this.form.invalid) return;

    const formData = new FormData();
    formData.append('name', this.form.value.name);
    formData.append('description', this.form.value.description);
    formData.append('location', this.form.value.location);
    formData.append('categoryId', this.form.value.categoryId);
    formData.append('cityId', this.form.value.cityId);
    formData.append('available', this.form.value.available);

    if (this.selectedFile) {
      formData.append('imageUri', this.selectedFile);
    }

    const observable = this.activityId
      ? this.activitesService.update(this.activityId, formData)
      : this.activitesService.create(formData);

    observable.subscribe({
      next: () => this.saved.emit(),
      error: (err) => console.error('Error saving activity:', err),
    });
  }

  onCancel() {
    this.cancelled.emit();
  }
}
