import { Component, ContentChildren, EventEmitter, Input, Output, QueryList, TemplateRef } from '@angular/core';
import { NgClass, NgForOf, NgIf, NgTemplateOutlet } from '@angular/common';
import { FormsModule } from '@angular/forms';


interface TableColumn {
  key: string;
  header: string;
  sortable?: boolean;
  minWidth?: string;
  maxWidth?: string;
  width?: string;
  primary?: boolean;
  cellTemplate?: TemplateRef<any>;
}

interface TableConfig {
  paginated?: boolean;
  searchable?: boolean;
  pageSize?: number;
}

@Component({
  selector: 'app-table',
  standalone: true,
  imports: [NgClass, NgForOf, NgTemplateOutlet, FormsModule, NgIf],
  templateUrl: './table.component.html',
  styleUrl: './table.component.scss',
})
export class TableComponent {
  @Input() columns: TableColumn[] = [];
  @Input() data: any[] = [];
  @Input() config: TableConfig = {};
  @Output() rowAction = new EventEmitter<{ action: string; item: any }>();

  @ContentChildren(TemplateRef) templates!: QueryList<TemplateRef<any>>;

  currentPage = 1;
  pageSize = 10;
  searchTerm = '';
  resizingColumn: TableColumn | null = null;

  get filteredData() {
    let filtered = this.data;

    if (this.searchTerm) {
      filtered = filtered.filter((item) =>
        this.columns.some((column) =>
          String(this.getProperty(item, column.key)).toLowerCase().includes(this.searchTerm.toLowerCase()),
        ),
      );
    }

    if (this.config.paginated) {
      const start = (this.currentPage - 1) * this.pageSize;
      const end = start + this.pageSize;
      return filtered.slice(start, end);
    }

    return filtered;
  }

  get totalItems() {
    return this.filteredData.length;
  }

  getProperty(obj: any, path: string) {
    return path.split('.').reduce((o, p) => o?.[p], obj);
  }

  startResize(event: MouseEvent, column: TableColumn) {
    this.resizingColumn = column;
    // Add resize logic here
  }

  previousPage() {
    if (this.currentPage > 1) this.currentPage--;
  }

  nextPage() {
    if (this.currentPage * this.pageSize < this.totalItems) this.currentPage++;
  }
}
