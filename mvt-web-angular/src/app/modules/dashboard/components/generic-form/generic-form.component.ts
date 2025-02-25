import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { NgForOf, NgIf, NgSwitch, NgSwitchCase } from '@angular/common';

export interface FormField {
  key: string;
  label: string;
  type: 'text' | 'textarea' | 'select' | 'checkbox' | 'file';
  required?: boolean;
  options?: { value: any; label: string }[];
  defaultValue?: any;
}

@Component({
  selector: 'app-generic-form',
  standalone: true,
  imports: [ReactiveFormsModule, NgSwitch, NgSwitchCase, NgIf, NgForOf],
  templateUrl: './generic-form.component.html',
  styleUrl: './generic-form.component.scss',
})
export class GenericFormComponent implements OnInit {
  @Input() formTitle: string = 'Form';
  @Input() formFields: FormField[] = [];
  @Input() initialData: any = {};
  @Input() submitButtonText: string = 'Submit';

  @Output() saved = new EventEmitter<any>();
  @Output() cancelled = new EventEmitter<void>();

  form: FormGroup;
  imagePreview?: string;

  constructor(private fb: FormBuilder) {
    this.form = this.fb.group({});
  }

  ngOnInit() {
    this.formFields.forEach((field) => {
      const validators = field.required ? [Validators.required] : [];
      this.form.addControl(field.key, this.fb.control(field.defaultValue || '', validators));
    });

    if (this.initialData) {
      this.form.patchValue(this.initialData);
      if (this.initialData.imageUri) {
        this.imagePreview = this.initialData.imageUri;
      }
    }
  }

  onFileSelected(event: any, key: string) {
    const file = event.target.files[0];
    if (file) {
      this.form.patchValue({ [key]: file });
      this.form.get(key)?.updateValueAndValidity();

      const reader = new FileReader();
      reader.onload = () => (this.imagePreview = reader.result as string);
      reader.readAsDataURL(file);
    }
  }

  onSubmit() {
    if (this.form.invalid) return;
    this.saved.emit(this.form.value);
  }

  onCancel() {
    this.cancelled.emit();
  }
}
