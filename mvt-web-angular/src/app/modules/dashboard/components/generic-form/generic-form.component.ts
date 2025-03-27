import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators, AbstractControl } from '@angular/forms';
import {NgClass, NgForOf, NgIf, NgSwitch, NgSwitchCase} from '@angular/common';
import { ValidationErrors } from '@angular/forms';

export interface FormField {
  key: string;
  label: string;
  type: 'text' | 'textarea' | 'select' | 'checkbox' | 'file';
  required?: boolean;
  options?: { value: any; label: string }[];
  defaultValue?: any;
  validators?: ValidationErrors;
  errorMessages?: {[key: string]: string};
}

@Component({
  selector: 'app-generic-form',
  standalone: true,
  imports: [ReactiveFormsModule, NgSwitch, NgSwitchCase, NgIf, NgForOf, NgClass],
  templateUrl: './generic-form.component.html',
  styleUrl: './generic-form.component.scss',
})
export class GenericFormComponent implements OnInit {
  @Input() formTitle: string = 'Form';
  @Input() formFields: FormField[] = [];
  @Input() initialData: any = {};
  @Input() submitButtonText: string = 'Submit';
  @Input() serverErrors: {[key: string]: string} = {};

  @Output() saved = new EventEmitter<any>();
  @Output() cancelled = new EventEmitter<void>();

  form: FormGroup = new FormGroup({});
  imagePreview?: string;
  submitted = false;

  constructor(private fb: FormBuilder) {}

  ngOnInit() {
    this.form = this.fb.group({});

    this.formFields.forEach((field) => {
      // Build validators array
      const validatorsList = [];

      if (field.required) {
        validatorsList.push(Validators.required);
      }

      if (field.validators) {
        Object.entries(field.validators).forEach(([key, value]) => {
          switch (key) {
            case 'minLength':
              validatorsList.push(Validators.minLength(value as number));
              break;
            case 'maxLength':
              validatorsList.push(Validators.maxLength(value as number));
              break;
            case 'pattern':
              validatorsList.push(Validators.pattern(value as string));
              break;
            case 'email':
              if (value) validatorsList.push(Validators.email);
              break;
          }
        });
      }

      this.form.addControl(field.key, this.fb.control(field.defaultValue || '', validatorsList));
    });

    if (this.initialData) {
      this.form.patchValue(this.initialData);
      if (this.initialData.imageUri) {
        this.imagePreview = this.initialData.imageUri;
      }
    }
  }

  onFileSelected(event: Event, fieldKey: string) {
    const input = event.target as HTMLInputElement;

    if (input.files && input.files.length > 0) {
      const file = input.files[0];

      const reader = new FileReader();
      reader.onload = () => {
        this.imagePreview = reader.result as string;
        this.form.get(fieldKey)?.setValue(file);
        this.form.get(fieldKey)?.markAsDirty();
      };
      reader.readAsDataURL(file);
    }
  }

  onSubmit() {
    this.submitted = true;

    if (this.form.invalid) {
      // Mark all fields as touched to trigger validation messages
      Object.keys(this.form.controls).forEach(key => {
        const control = this.form.get(key);
        control?.markAsTouched();
      });
      return;
    }

    this.saved.emit(this.form.value);
  }

  onCancel() {
    this.cancelled.emit();
  }

  isFieldInvalid(fieldName: string): boolean {
    const control = this.form.get(fieldName);
    return !!(control && control.invalid && (control.touched || this.submitted));
  }

  getErrorMessage(fieldName: string): string {
    const field = this.formFields.find(f => f.key === fieldName);
    const control = this.form.get(fieldName);

    if (!control || !control.errors) return '';

    if (this.serverErrors[fieldName]) {
      return this.serverErrors[fieldName];
    }

    // Then check for client-side validation errors
    const errors = Object.keys(control.errors);
    if (errors.length === 0) return '';

    // Use custom error messages if provided
    if (field?.errorMessages && field.errorMessages[errors[0]]) {
      return field.errorMessages[errors[0]];
    }

    switch (errors[0]) {
      case 'required':
        return `${field?.label || fieldName} is required`;
      case 'minlength':
        const minLength = control.errors['minlength'].requiredLength;
        return `${field?.label || fieldName} must be at least ${minLength} characters`;
      case 'maxlength':
        const maxLength = control.errors['maxlength'].requiredLength;
        return `${field?.label || fieldName} cannot exceed ${maxLength} characters`;
      case 'pattern':
        return `${field?.label || fieldName} has an invalid format`;
      case 'email':
        return `Please enter a valid email address`;
      default:
        return `${field?.label || fieldName} is invalid`;
    }
  }
}
