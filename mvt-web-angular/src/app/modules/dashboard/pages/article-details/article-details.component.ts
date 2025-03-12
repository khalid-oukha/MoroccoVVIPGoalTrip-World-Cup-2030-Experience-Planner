import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { ArticlesService } from '../../../../core/services/articles.service';
import { Article } from '../../../../core/models/Article';

@Component({
  selector: 'app-article-details',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="p-6">
      <div class="mb-4 flex justify-between items-center">
        <button
          (click)="goBack()"
          class="flex items-center text-primary hover:text-primary/80 transition-colors">
          <svg class="w-5 h-5 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 19l-7-7m0 0l7-7m-7 7h18"/>
          </svg>
          Back to Articles
        </button>

        <div class="flex gap-2">
          <button
            (click)="editArticle()"
            class="px-4 py-2 bg-primary text-primary-foreground rounded hover:bg-primary/90 transition-colors">
            Edit
          </button>
          <button
            (click)="deleteArticle()"
            class="px-4 py-2 bg-destructive text-destructive-foreground rounded hover:bg-destructive/90 transition-colors">
            Delete
          </button>
        </div>
      </div>

      <div *ngIf="isLoading" class="flex justify-center py-12">
        <div class="inline-block animate-spin rounded-full h-8 w-8 border-b-2 border-primary"></div>
      </div>

      <div *ngIf="article && !isLoading" class="bg-card rounded-lg shadow-sm overflow-hidden">
        <div *ngIf="article.imageUrl" class="w-full h-64 bg-cover bg-center"
             [style.background-image]="'url(' + article.imageUrl + ')'">
        </div>

        <div class="p-6">
          <div class="mb-4 text-sm text-muted-foreground">
            Created: {{ article.createdAt | date:'medium' }}
          </div>

          <div class="prose max-w-none">
            <p [innerHTML]="formatContent(article.content)"></p>
          </div>
        </div>
      </div>

      <div *ngIf="error" class="mt-4 p-4 bg-destructive/10 text-destructive rounded-md">
        {{ error }}
      </div>
    </div>
  `,
})
export class ArticleDetailsComponent implements OnInit {
  article: Article | null = null;
  isLoading = true;
  error: string | null = null;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private articlesService: ArticlesService
  ) {}

  ngOnInit() {
    const articleId = this.route.snapshot.paramMap.get('id');
    if (articleId) {
      this.loadArticle(articleId);
    } else {
      this.error = 'Article ID not provided';
      this.isLoading = false;
    }
  }

  loadArticle(id: string) {
    this.isLoading = true;
    this.articlesService.findById(id).subscribe({
      next: (article) => {
        this.article = article;
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error loading article:', err);
        this.error = 'Failed to load article details';
        this.isLoading = false;
      }
    });
  }

  goBack() {
    this.router.navigate(['/dashboard/articles']);
  }

  editArticle() {
    // Navigate back to articles list with a query param to open edit modal
    // This is one approach - alternatively you could implement an edit view
    this.router.navigate(['/dashboard/articles'], {
      queryParams: { edit: this.article?.id }
    });
  }

  deleteArticle() {
    if (!this.article) return;

    if (confirm('Are you sure you want to delete this article?')) {
      this.isLoading = true;
      this.articlesService.delete(this.article.id).subscribe({
        next: () => {
          this.router.navigate(['/dashboard/articles']);
        },
        error: (err) => {
          console.error('Error deleting article:', err);
          this.error = 'Failed to delete article';
          this.isLoading = false;
        }
      });
    }
  }

  formatContent(content: string): string {
    return content.replace(/\n\n/g, '</p><p>').replace(/\n/g, '<br>');
  }
}
