import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Router} from '@angular/router';
import {AuthService} from '../../../../core/services/auth.service';

@Component({
  selector: 'app-sign-in',
  standalone: false,

  templateUrl: './sign-in.component.html',
  styleUrl: './sign-in.component.scss'
})
export class SignInComponent implements OnInit {
  form!: FormGroup;
  submitted = false;
  passwordTextType!: boolean;

  constructor(private authService:AuthService,
              private fb:FormBuilder,
              private router:Router) {

  }
  onClick() {
    console.log('Button clicked');
  }

  ngOnInit(): void {
    this.form = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
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
    if (this.form.invalid) return;

    const { email, password } = this.form.value;
console.log('email:', email);
console.log('password:', password);
    this.authService.login({ email, password }).subscribe({
      next: () => {
        // Navigate only on successful login
        this.router.navigate(['/']);
      },
      error: (error) => {
        console.error('Login failed:', error);
        // Add error message handling here
      }
    });
  }
}
