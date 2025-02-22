import { Component } from '@angular/core';
import { NgIf } from '@angular/common';

@Component({
  selector: 'app-profile-menu',
  standalone: true,
  imports: [NgIf],
  templateUrl: './profile-menu.component.html',
  styleUrl: './profile-menu.component.scss',
})
export class ProfileMenuComponent {
  isMenuOpen = false;

  toggleMenu() {
    this.isMenuOpen = !this.isMenuOpen;
  }
}
