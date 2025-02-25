import { Component, OnInit } from '@angular/core';
import { TableComponent } from '../../components/table/table.component';
import { CategoriesService } from '../../../../core/services/categories.service';
import { Category } from '../../../../core/models/Category';
import { FormField, GenericFormComponent } from '../../components/generic-form/generic-form.component';
import { NgIf } from '@angular/common';

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
  formFields: FormField[] = [
    { key: 'name', label: 'Name', type: 'text' },
    { key: 'description', label: 'Description', type: 'textarea'},
    { key: 'imageUri', label: 'Image', type: 'file' },
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
    this.categoriesService.findAll().subscribe({
      next: (categories) => (this.categories = categories),
      error: (err) => console.error('Error loading categories:', err),
    });
  }

  handleEdit(category: Category) {
    this.selectedCategory = category;
    this.showForm = true;
  }

  handleDelete(category: Category) {
    if (confirm(`Delete ${category.name}?`)) {
      this.categoriesService.delete(category.id).subscribe({
        next: () => this.loadCategories(),
        error: (err) => console.error('Delete failed:', err),
      });
    }
  }

  handleFormSubmit(formData: any) {
    const formDataObj = new FormData();
    formDataObj.append('name', formData.name.trim());
    formDataObj.append('description', formData.description.trim());

    if (formData.imageUri instanceof File) {
      formDataObj.append('imageUri', formData.imageUri);
    }

    const operation = this.selectedCategory
      ? this.categoriesService.update(this.selectedCategory.id, formDataObj)
      : this.categoriesService.create(formDataObj);

    operation.subscribe({
      next: () => {
        this.showForm = false;
        this.selectedCategory = null;
        this.loadCategories();
      },
      error: (err) => console.error('Save failed:', err),
    });
  }



  handleFormCancel() {
    this.showForm = false;
    this.selectedCategory = null;
  }
}
