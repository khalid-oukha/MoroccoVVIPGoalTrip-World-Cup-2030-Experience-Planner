import { Component } from '@angular/core';
import { NgIf } from '@angular/common';
import { AuthService } from '../../../../core/services/auth.service';

@Component({
  selector: 'app-profile-menu',
  standalone: true,
  imports: [NgIf],
  templateUrl: './profile-menu.component.html',
  styleUrl: './profile-menu.component.scss',
})
export class ProfileMenuComponent {
  isMenuOpen = false;
  constructor(protected authService: AuthService) {
  }
  toggleMenu() {
    this.isMenuOpen = !this.isMenuOpen;
  }


}
