import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss'
})
export class RegisterComponent {
  name = signal<string>('');
  email = signal<string>('');
  password = signal<string>('');
  confirmPassword = signal<string>('');
  error = signal<string>('');
  showPassword = signal<boolean>(false);

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  register(): void {
    const name = this.name().trim();
    const email = this.email().trim();
    const password = this.password();
    const confirmPassword = this.confirmPassword();

    if (!name || !email || !password) {
      this.error.set('Please fill in all fields');
      return;
    }

    if (password.length < 6) {
      this.error.set('Password must be at least 6 characters');
      return;
    }

    if (password !== confirmPassword) {
      this.error.set('Passwords do not match');
      return;
    }

//     this.authService.register(name, email, password).subscribe({
//       next: () => {
//         this.error.set('');
//         this.router.navigate(['/login']);
//       },
//       error: () => {
//         this.error.set('Registration failed. Email may already be in use.');
//       }
//     });
      this.authService.register(name, email, password).subscribe({
        next: (resp) => {
          this.error.set('');
          // Navigate to login with success message
          this.router.navigate(['/login'], { state: { registered: true } });
        },
        error: (err) => {
          // Check if it's a 409 (email already exists)
          if (err.status === 409 && err.error && err.error.message) {
            this.error.set(err.error.message); // "Email already registered"
          } else if (err.error && err.error.message) {
            this.error.set(err.error.message);
          } else {
            this.error.set('Registration failed. Please try again later.');
          }
        }
      });
  }

  togglePassword(): void {
    this.showPassword.update(v => !v);
  }
}

