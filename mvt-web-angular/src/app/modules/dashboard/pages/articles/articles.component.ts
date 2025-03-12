import { Component, OnInit } from '@angular/core';
import { TableComponent } from '../../components/table/table.component';
import { ArticlesService } from '../../../../core/services/articles.service';
import { Article } from '../../../../core/models/Article';
import { FormField, GenericFormComponent } from '../../components/generic-form/generic-form.component';
import { NgIf } from '@angular/common';
import { ActivitesService } from '../../../../core/services/activites.service';

@Component({
  selector: 'app-articles',
  standalone: true,
  imports: [TableComponent, GenericFormComponent, NgIf],
  templateUrl: './articles.component.html',
  styleUrl: './articles.component.scss',
})
export class ArticlesComponent implements OnInit {
  articles: Article[] = [];
  showForm = false;
  selectedArticle: Article | null = null;
  isLoading = false;

  formFields: FormField[] = [
    { key: 'content', label: 'Content', type: 'textarea', required: true },
    { key: 'image', label: 'Image', type: 'file' },
    {
      key: 'activityId',
      label: 'Activity',
      type: 'select',
      options: [] // Will be populated in loadActivities
    }
  ];

  columns = [
    { key: 'imageUrl', header: 'Image' },
    { key: 'contentPreview', header: 'Content' },
    { key: 'createdAt', header: 'Created Date' },
    { key: 'actions', header: 'Actions' }
  ];

  constructor(
    private articlesService: ArticlesService,
    private activitiesService: ActivitesService
  ) {}

  ngOnInit() {
    this.loadArticles();
    this.loadActivities();
  }

  loadArticles() {
    this.isLoading = true;
    this.articlesService.findAll().subscribe({
      next: (response) => {
        // Add a content preview property for table display
        this.articles = response.content.map(article => ({
          ...article,
          contentPreview: article.content.length > 100
            ? article.content.substring(0, 100) + '...'
            : article.content
        }));
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error loading articles:', err);
        this.isLoading = false;
      }
    });
  }

  loadActivities() {
    this.activitiesService.findAll().subscribe({
      next: (response) => {
        // Find the activity field and update its options
        const activityField = this.formFields.find(field => field.key === 'activityId');
        if (activityField) {
          activityField.options = [
            { value: '', label: 'None' },
            ...response.content.map(activity => ({
              value: activity.id,
              label: activity.name
            }))
          ];
        }
      },
      error: (err) => console.error('Error loading activities:', err)
    });
  }

  handleEdit(article: Article) {
    this.articlesService.findById(article.id).subscribe({
      next: (fullArticle) => {
        this.selectedArticle = fullArticle;
        this.showForm = true;
      },
      error: (err) => console.error('Error loading article details:', err)
    });
  }

  handleDelete(article: Article) {
    if (confirm(`Delete this article?`)) {
      this.isLoading = true;
      this.articlesService.delete(article.id).subscribe({
        next: () => {
          this.loadArticles();
        },
        error: (err) => {
          console.error('Delete failed:', err);
          this.isLoading = false;
        }
      });
    }
  }

  handleFormSubmit(formData: any) {
    this.isLoading = true;
    const formDataObj = this.articlesService.prepareFormData(formData);

    const operation = this.selectedArticle
      ? this.articlesService.update(this.selectedArticle.id, formDataObj)
      : this.articlesService.create(formDataObj);

    operation.subscribe({
      next: () => {
        this.showForm = false;
        this.selectedArticle = null;
        this.loadArticles();
      },
      error: (err) => {
        console.error('Save failed:', err);
        this.isLoading = false;
      }
    });
  }

  handleFormCancel() {
    this.showForm = false;
    this.selectedArticle = null;
  }
}
