// table.component.ts
import { Component, EventEmitter, Input, Output, TemplateRef } from '@angular/core';
import { NgClass, NgFor, NgIf, NgTemplateOutlet } from '@angular/common';

interface TableColumn {
  key: string;
  header: string;
  minWidth?: string;
  cellTemplate?: TemplateRef<any>| null;
}

@Component({
  selector: 'app-table',
  standalone: true,
  imports: [NgFor, NgIf, NgClass, NgTemplateOutlet],
  templateUrl: './table.component.html',
  styleUrls: ['./table.component.scss'],
})
export class TableComponent {
  @Input() columns: TableColumn[] = [];
  @Input() data: any[] = [];
  @Output() edit = new EventEmitter<any>();
  @Output() delete = new EventEmitter<any>();
  @Output() viewDetails = new EventEmitter<any>();

  getProperty(obj: any, path: string): string {
    return path.split('.').reduce((o, p) => o?.[p], obj) || '';
  }

  isImage(value: any): boolean {
    return typeof value === 'string' && (value.startsWith('http') || value.startsWith('data:image'));
  }
}
