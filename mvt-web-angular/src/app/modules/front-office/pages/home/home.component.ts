import { Component } from '@angular/core';
import { FooterComponent } from '../../../layout/components/footer/footer.component';
import { NavbarComponent } from '../../../layout/components/navbar/navbar.component';
import { RouterOutlet } from '@angular/router';
import { SidebarComponent } from '../../../layout/components/sidebar/sidebar.component';
import { HeaderComponent } from '../../components/header/header.component';
import { NgIf, NgOptimizedImage } from '@angular/common';
import { AuthService } from '../../../../core/services/auth.service';
import { ActivityCardComponent } from '../../components/activity-card/activity-card.component';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [NgOptimizedImage, NgIf, ActivityCardComponent],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss',
})
export class HomeComponent {
  constructor(public authService: AuthService) {}
}
