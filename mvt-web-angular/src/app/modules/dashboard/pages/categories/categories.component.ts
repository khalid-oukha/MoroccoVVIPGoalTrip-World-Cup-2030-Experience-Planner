import { Component, OnInit } from '@angular/core';
import { TableComponent } from '../../components/table/table.component';
import { CategoriesService } from '../../../../core/services/categories.service';
import { Category } from '../../../../core/models/Category';
import { FormField, GenericFormComponent } from '../../components/generic-form/generic-form.component';
import { NgIf } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { catchError } from 'rxjs/operators';
import { of } from 'rxjs';

@Component({
  selector: 'app-categories',
  standalone: true,
  imports: [TableComponent, GenericFormComponent, NgIf],
  templateUrl: './categories.component.html',
  styleUrl: './categories.component.scss',
})
export class CategoriesComponent implements OnInit {
  categories: Category[] = [];
  showForm = false;
  selectedCategory: Category | null = null;
  serverErrors: {[key: string]: string} = {};
  isLoading = false;

  formFields: FormField[] = [
    {
      key: 'name',
      label: 'Name',
      type: 'text',
      required: true,
      validators: { minLength: 3, maxLength: 50 },
      errorMessages: {
        required: 'Category name is required',
        minLength: 'Name must be at least 3 characters',
        maxLength: 'Name cannot exceed 50 characters'
      }
    },
    {
      key: 'description',
      label: 'Description',
      type: 'textarea',
      validators: { maxLength: 500 },
      errorMessages: {
        maxLength: 'Description cannot exceed 500 characters'
      }
    },
    {
      key: 'imageUri',
      label: 'Image',
      type: 'file',
      required: true,
      errorMessages: {
        required: 'Please select an image'
      }
    },
  ];

  columns = [
    { key: 'imageUri', header: 'Image' },
    { key: 'name', header: 'Name' },
    { key: 'description', header: 'Description' },
    { key: 'actions', header: 'Actions' },
  ];

  constructor(private categoriesService: CategoriesService) {}

  ngOnInit() {
    this.loadCategories();
  }

  loadCategories() {
    this.isLoading = true;
    this.categoriesService.findAll().subscribe({
      next: (categories) => {
        this.categories = categories;
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error loading categories:', err);
        this.isLoading = false;
      },
    });
  }

  handleEdit(category: Category) {
    this.selectedCategory = category;
    this.serverErrors = {};
    this.showForm = true;
  }

  handleDelete(category: Category) {
    if (confirm(`Delete ${category.name}?`)) {
      this.isLoading = true;
      this.categoriesService.delete(category.id).subscribe({
        next: () => {
          this.loadCategories();
          this.isLoading = false;
        },
        error: (err) => {
          console.error('Delete failed:', err);
          this.isLoading = false;
        },
      });
    }
  }

  handleFormSubmit(formData: any) {
    this.isLoading = true;
    this.serverErrors = {};

    const formDataObj = new FormData();

    // Only append non-null values
    if (formData.name) {
      formDataObj.append('name', formData.name.trim());
    }

    if (formData.description) {
      formDataObj.append('description', formData.description.trim());
    }

    if (formData.imageUri instanceof File) {
      formDataObj.append('imageUri', formData.imageUri);
    }

    const operation = this.selectedCategory
      ? this.categoriesService.update(this.selectedCategory.id, formDataObj)
      : this.categoriesService.create(formDataObj);

    operation
      .pipe(
        catchError((error: HttpErrorResponse) => {
          this.isLoading = false;

          if (error.status === 400 && error.error) {
            this.handleServerValidationErrors(error.error);
          } else {
            alert('An error occurred. Please try again.');
          }

          return of(null);
        })
      )
      .subscribe({
        next: (result) => {
          if (result) {
            this.showForm = false;
            this.selectedCategory = null;
            this.loadCategories();
          }
          this.isLoading = false;
        }
      });
  }

  handleServerValidationErrors(errorResponse: any) {
    this.serverErrors = {};

    if (errorResponse.errors) {
      errorResponse.errors.forEach((error: any) => {
        this.serverErrors[error.field] = error.defaultMessage;
      });
    }
    else if (typeof errorResponse === 'object') {
      Object.keys(errorResponse).forEach(key => {
        this.serverErrors[key] = errorResponse[key];
      });
    }
    else if (errorResponse.message && errorResponse.message.includes('already exists')) {
      this.serverErrors['name'] = 'Category with this name already exists';
    }
  }

  handleFormCancel() {
    this.showForm = false;
    this.selectedCategory = null;
    this.serverErrors = {};
  }
}
