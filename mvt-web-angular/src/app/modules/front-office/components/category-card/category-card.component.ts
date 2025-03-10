import { Component, Input } from '@angular/core';
import { NgOptimizedImage } from '@angular/common';
import {Category} from "../../../../core/models/Category";

@Component({
  selector: 'app-category-card',
  standalone: true,
  imports: [NgOptimizedImage],
  templateUrl: './category-card.component.html',
  styleUrls: ['./category-card.component.scss']
})
export class CategoryCardComponent {
  @Input({ required: true }) category: any;

}
