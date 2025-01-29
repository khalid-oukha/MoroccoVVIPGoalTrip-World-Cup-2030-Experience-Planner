import { Component } from '@angular/core';
import {ThemeService} from './core/services/theme.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  standalone: false,
  styleUrl: './app.component.scss'
})
export class AppComponent {
  title = 'mvp-web-angular';
  constructor(public themeService: ThemeService) {}
}
