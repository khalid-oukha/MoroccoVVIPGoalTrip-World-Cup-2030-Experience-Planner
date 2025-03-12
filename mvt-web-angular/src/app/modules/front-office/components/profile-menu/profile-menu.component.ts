import { Component, OnInit } from '@angular/core';
import { NgIf } from '@angular/common';
import { AuthService } from '../../../../core/services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-profile-menu',
  standalone: true,
  imports: [NgIf],
  templateUrl: './profile-menu.component.html',
  styleUrl: './profile-menu.component.scss',
})
export class ProfileMenuComponent implements OnInit {
  isMenuOpen = false;

  constructor(
    protected authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    // Ensure user details are loaded
    if (this.authService.isLoggedIn() && !this.authService.getCurrentUser()) {
      this.authService.fetchUserDetails().subscribe();
    }
  }

  toggleMenu() {
    this.isMenuOpen = !this.isMenuOpen;
  }

  getUserInitials(): string {
    const user = this.authService.getCurrentUser();
    if (!user) return '';

    const firstInitial = user.firstName.charAt(0);
    const lastInitial = user.lastName.charAt(0);

    return `${firstInitial}${lastInitial}`;
  }

  getFullName(): string {
    const user = this.authService.getCurrentUser();
    return user ? `${user.firstName} ${user.lastName}` : '';
  }

  getEmail(): string {
    const user = this.authService.getCurrentUser();
    return user ? user.email : '';
  }

  logout(): void {
    this.authService.logout();
    this.isMenuOpen = false;
    this.router.navigate(['/auth/sign-in']);
  }
}
