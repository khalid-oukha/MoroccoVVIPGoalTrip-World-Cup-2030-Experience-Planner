import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { NgClass, NgIf } from '@angular/common';
import { AngularSvgIconModule } from 'angular-svg-icon';
import { ButtonComponent } from '../../../../shared/components/button/button.component';
import { AuthService } from '../../../../core/services/auth.service';

@Component({
  selector: 'app-sign-in',
  templateUrl: './sign-in.component.html',
  styleUrls: ['./sign-in.component.scss'],
  standalone: true,
  imports: [FormsModule, ReactiveFormsModule, RouterLink, AngularSvgIconModule, NgClass, NgIf, ButtonComponent],
})
export class SignInComponent implements OnInit {
  form!: FormGroup;
  submitted = false;
  passwordTextType!: boolean;
  isLoading = false;
  errorMessage: string | null = null;
  successMessage: string | null = null;

  constructor(
    private readonly _formBuilder: FormBuilder,
    private readonly _router: Router,
    private readonly _authService: AuthService
  ) {}

  ngOnInit(): void {
    this.form = this._formBuilder.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required],
    });
  }

  get f() {
    return this.form.controls;
  }

  togglePasswordTextType() {
    this.passwordTextType = !this.passwordTextType;
  }

  onSubmit() {
    this.submitted = true;
    this.errorMessage = null; // Reset error message
    this.successMessage = null; // Reset success message

    // Stop here if the form is invalid
    if (this.form.invalid) {
      return;
    }

    this.isLoading = true; // Set loading state to true

    const { email, password } = this.form.value;

    // Call the AuthService login method
    this._authService.login({ email, password }).subscribe({
      next: (user) => {
        this.isLoading = false; // Reset loading state
        this.successMessage = 'Login successful!'; // Set success message
        this._router.navigate(['/dashboard']); // Navigate to the dashboard
      },
      error: (error) => {
        this.isLoading = false; // Reset loading state
        this.errorMessage = 'Login failed. Please check your credentials.'; // Set error message
        console.error('Login error:', error);
      },
    });
  }
}
