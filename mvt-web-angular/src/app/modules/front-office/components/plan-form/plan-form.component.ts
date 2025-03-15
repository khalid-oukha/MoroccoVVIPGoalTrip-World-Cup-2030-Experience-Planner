import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { FormField, GenericFormComponent } from "../../../dashboard/components/generic-form/generic-form.component";

@Component({
  selector: 'app-plan-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, GenericFormComponent],
  templateUrl: './plan-form.component.html',
  styleUrls: ['./plan-form.component.scss']
})
export class PlanFormComponent implements OnInit {
  @Input() plan: any = null;
  @Input() isEdit = false;

  @Output() saveEvent = new EventEmitter<any>();
  @Output() cancelEvent = new EventEmitter<void>();

  formFields: FormField[] = [];
  formTitle = 'Create New Plan';

  ngOnInit(): void {
    this.formFields = [
      {
        key: 'name',
        label: 'Plan Name',
        type: 'text',
        required: true
      },
      {
        key: 'description',
        label: 'Description',
        type: 'textarea',
        required: true
      },
      {
        key: 'imageUri',
        label: 'Cover Image',
        type: 'file'
      }
    ];

    this.formTitle = this.isEdit ? 'Edit Plan' : 'Create New Plan';
  }

  onSave(formData: any): void {
    const data = new FormData();
    data.append('name', formData.name);
    data.append('description', formData.description);

    if (formData.imageUri && typeof formData.imageUri !== 'string') {
      data.append('imageUri', formData.imageUri);
    }

    if (this.isEdit && this.plan) {
      this.saveEvent.emit({ id: this.plan.id, data });
    } else {
      this.saveEvent.emit(data);
    }
  }

  onCancel(): void {
    this.cancelEvent.emit();
  }
}
