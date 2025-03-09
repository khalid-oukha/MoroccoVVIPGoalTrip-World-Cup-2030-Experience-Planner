import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { Subject, debounceTime, switchMap, takeUntil, combineLatest } from 'rxjs';
import { ActivitesService } from '../../../../core/services/activites.service';
import { CategoriesService } from '../../../../core/services/categories.service';
import { CitiesService } from '../../../../core/services/cities.service';
import { NgClass, NgForOf, NgIf } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { ActivityCardComponent } from '../../components/activity-card/activity-card.component';
import { Activity } from "../../../../core/models/Activity";
import { Category } from "../../../../core/models/Category";
import { City } from "../../../../core/models/City";
import { Page } from "../../../../core/models/Page";

@Component({
  selector: 'app-activities',
  standalone: true,
  imports: [NgClass, NgForOf, NgIf, FormsModule, RouterLink, ActivityCardComponent],
  templateUrl: './activities.component.html',
  styleUrls: ['./activities.component.scss']
})
export class ActivitiesComponent implements OnInit, OnDestroy {
  private destroy$ = new Subject<void>();
  private filter$ = new Subject<void>();

  activities: Activity[] = [];
  categories: Category[] = [];
  cities: City[] = [];
  page: Page<Activity> | null = null;

  state = {
    page: 0,
    size: 12,
    sort: 'id,asc',
    cityId: null as number | null,
    categoryId: null as number | null,
    available: null as boolean | null,
    search: ''
  };

  loading = false;
  error = false;

  constructor(
    private router: Router,
    private activitiesService: ActivitesService,
    private categoriesService: CategoriesService,
    private citiesService: CitiesService
  ) {}

  ngOnInit() {
    this.initDataStreams();
    this.loadStaticData();
  }

  private initDataStreams() {
    this.filter$.pipe(
      debounceTime(300),
      switchMap(async () => this.loadActivities()),
      takeUntil(this.destroy$)
    ).subscribe();

    this.filter$.next();
  }

  private loadStaticData() {
    combineLatest([
      this.categoriesService.findAll(),
      this.citiesService.findAll()
    ]).pipe(takeUntil(this.destroy$)).subscribe({
      next: ([categories, cities]) => {
        this.categories = categories;
        this.cities = cities;
      },
      error: (error) => console.error('Error loading static data:', error)
    });
  }

  protected loadActivities() {
    this.loading = true;
    this.error = false;

    return this.activitiesService.findAll(
      this.state.cityId ?? undefined,
      this.state.categoryId ?? undefined,
      this.state.available ?? undefined,
      this.state.search.trim(),
      this.state.page,
      this.state.size,
      this.state.sort
    ).pipe(takeUntil(this.destroy$)).subscribe({
      next: (page) => {
        this.page = page;
        this.activities = page.content;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading activities:', error);
        this.loading = false;
        this.error = true;
      }
    });
  }

  onFilterChange() {
    this.state.page = 0;
    this.filter$.next();
  }

  onPageChange(page: number) {
    if (this.page && page >= 0 && page < this.page.totalPages) {
      this.state.page = page;
      this.filter$.next();
      window.scrollTo({ top: 0, behavior: 'smooth' });
    }
  }

  resetFilters() {
    // Reset the state to its initial values
    this.state = {
      page: 0,
      size: 12,
      sort: 'id,asc',
      cityId: null,
      categoryId: null,
      available: null,
      search: ''
    };
    this.filter$.next();
  }

  // Added safe null check method for pagination range
  getPaginationRange(): number[] {
    if (!this.page) return [];

    const totalPages = this.page.totalPages;
    const current = this.state.page;
    const range: number[] = [];

    if (totalPages <= 7) {
      for (let i = 0; i < totalPages; i++) range.push(i);
    } else {
      range.push(0);
      if (current > 2) range.push(-1);
      const start = Math.max(1, current - 1);
      const end = Math.min(totalPages - 2, current + 1);
      for (let i = start; i <= end; i++) range.push(i);
      if (current < totalPages - 3) range.push(-1);
      range.push(totalPages - 1);
    }
    return range;
  }

  // Safe method to get total pages
  get totalPages(): number {
    return this.page?.totalPages ?? 0;
  }

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }

  getCityName(cityId: number | null): string {
    if (!cityId) return '';
    return this.cities.find(c => c.id === cityId)?.name || '';
  }

  getCategoryName(categoryId: number | null): string {
    if (!categoryId) return '';
    return this.categories.find(c => c.id === categoryId)?.name || '';
  }

  trackByActivityId(index: number, activity: Activity): string {
    return activity.id;
  }

  onAddToPlan(activity: Activity) {
    if (activity.available) {
      console.log('Adding to plan:', activity.name);
    }
  }

  onViewDetails(activity: Activity) {
    this.router.navigate(['/activities', activity.id]);
  }
}
