import { Category } from './Category';
import { City } from './City';
import { Article } from './Article';

export interface Activity {
  id: string;
  name: string;
  description: string;
  location: string;
  available: boolean;
  createdAt: string;
  category: Category;
  city: City;
  article: Article;
  imageUri: string;
}
