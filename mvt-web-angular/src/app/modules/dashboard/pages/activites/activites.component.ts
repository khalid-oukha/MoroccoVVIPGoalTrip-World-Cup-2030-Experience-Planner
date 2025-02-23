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

@Component({
  selector: 'app-activites',
  standalone: true,
  imports: [TableComponent, NgClass, FormsModule, NgForOf, NgIf],
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

  // Filters
  cityId: number | null = null;
  categoryId: number | null = null;
  available: boolean | null = null;
  search: string = '';

  columns = [
    { key: 'name', header: 'Name' },
    { key: 'city.name', header: 'City' },
    { key: 'category.name', header: 'Category' },
    { key: 'available', header: 'Status' },
    { key: 'category.imageUri', header: 'Image' },
  ];

  constructor(
    private activitiesService: ActivitesService,
    private categoriesService: CategoriesService,
    private citiesService: CitiesService
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
    this.activitiesService.findAll(
      this.cityId ?? undefined,
      this.categoryId ?? undefined,
      this.available ?? undefined,
      this.search.trim() || undefined, // Use undefined instead of null
      this.currentPage,
      this.pageSize,
      this.sort
    ).subscribe({
      next: (page) => {
        this.page = page;
        this.activities = page.content;
      },
      error: (error) => console.error('Error fetching activities:', error),
    });
  }

  onPageChange(page: number) {
    if (page >= 0) {
      this.currentPage = page;
      this.loadActivities();
    }
  }

  onFilterChange() {
    this.currentPage = 0; // Reset to first page on filter change
    this.loadActivities();
  }

  onSortChange(sort: string) {
    this.sort = sort;
    this.loadActivities();
  }

  onEdit(activity: Activity) {
    // Handle edit logic
  }

  onDelete(activity: Activity) {
    // Handle delete logic
  }

  onViewDetails(activity: Activity) {
    // Handle view details logic
  }
}
