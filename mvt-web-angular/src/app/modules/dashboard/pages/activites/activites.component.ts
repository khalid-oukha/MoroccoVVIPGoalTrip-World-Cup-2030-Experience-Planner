import { Component, OnInit } from '@angular/core';
import { TableComponent } from '../../components/table/table.component';
import { Activity } from '../../../../core/models/Activity';
import { ActivitesService } from '../../../../core/services/activites.service';
import { Page } from '../../../../core/models/Page';
import { NgClass, NgForOf, NgIf } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Category } from '../../../../core/models/Category';
import { CategoriesService } from '../../../../core/services/categories.service';
import { City } from '../../../../core/models/City';
import { CitiesService } from '../../../../core/services/cities.service';
import { FormComponent } from '../../components/form/form.component';
import { Router } from '@angular/router';

@Component({
  selector: 'app-activites',
  standalone: true,
  imports: [TableComponent, NgClass, FormsModule, NgForOf, NgIf, FormComponent],
  templateUrl: './activites.component.html',
  styleUrl: './activites.component.scss',
})
export class ActivitesComponent implements OnInit {
  activities: Activity[] = [];
  categories: Category[] = [];
  cities: City[] = [];
  page: Page<Activity> | null = null;
  currentPage = 0;
  pageSize = 10;
  sort = 'id,asc';

  cityId: number | null = null;
  categoryId: number | null = null;
  available: boolean | null = null;
  search: string = '';
  showForm = false;
  selectedActivityId?: string;
  showDetails = false;
  selectedActivityDetails?: Activity;


  columns = [
    { key: 'imageUri', header: 'Image' },
    { key: 'name', header: 'Name' },
    { key: 'city.name', header: 'City' },
    { key: 'category.name', header: 'Category' },
    { key: 'available', header: 'Status' },
  ];

  constructor(
    private activitiesService: ActivitesService,
    private categoriesService: CategoriesService,
    private citiesService: CitiesService,
    private router: Router
  ) {}

  ngOnInit() {
    this.loadCategories();
    this.loadCities();
    this.loadActivities();
  }

  loadCities() {
    this.citiesService.findAll().subscribe({
      next: (cities) => {
        this.cities = cities;
      },
      error: (error) => console.error('Error fetching cities:', error),
    });
  }

  loadCategories() {
    this.categoriesService.findAll().subscribe({
      next: (categories) => {
        this.categories = categories;
      },
      error: (error) => console.error('Error fetching categories:', error),
    });
  }

  loadActivities() {
    this.activitiesService
      .findAll(
        this.cityId ?? undefined,
        this.categoryId ?? undefined,
        this.available ?? undefined,
        this.search.trim() || undefined,
        this.currentPage,
        this.pageSize,
        this.sort,
      )
      .subscribe({
        next: (page) => {
          this.page = page;
          this.activities = page.content;
          console.log('Activities:', this.activities);
        },
        error: (error) => console.error('Error fetching activities:', error),
      });
  }

  onAddNew() {
    this.showForm = true;
    this.selectedActivityId = undefined;
  }

  onFormSaved() {
    this.showForm = false;
    this.loadActivities();
  }

  onFormCancelled() {
    this.showForm = false;
  }

  onEdit(activity: Activity) {
    this.selectedActivityId = activity.id;
    this.showForm = true;
  }

  onPageChange(page: number) {
    if (page >= 0) {
      this.currentPage = page;
      this.loadActivities();
    }
  }

  onFilterChange() {
    this.currentPage = 0;
    this.loadActivities();
  }

  onViewDetails(activity: Activity) {
    this.router.navigate(['/dashboard/activities', activity.id]);
  }
  onCloseDetails() {
    this.showDetails = false;
    this.selectedActivityDetails = undefined;
  }

  onSortChange(sort: string) {
    this.sort = sort;
    this.loadActivities();
  }

  onDelete(activity: Activity) {
    if (confirm(`Are you sure you want to delete "${activity.name}"?`)) {
      this.activitiesService.delete(activity.id).subscribe({
        next: () => {
          if (this.page) {
            this.page.totalElements--;
            this.page.content = this.page.content.filter(a => a.id !== activity.id);
          }
          this.activities = this.activities.filter(a => a.id !== activity.id);
        },
        error: (err) => {
          console.error('Delete failed:', err);
          alert('Error deleting activity');
        }
      });
    }
  }

}
