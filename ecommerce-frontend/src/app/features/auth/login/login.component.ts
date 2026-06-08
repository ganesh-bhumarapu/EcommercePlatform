import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {
  email = signal<string>('');
  password = signal<string>('');
  error = signal<string>('');
  showPassword = signal<boolean>(false);

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  login(): void {
    const email = this.email().trim();
    const password = this.password();

    if (!email || !password) {
      this.error.set('Please enter both email and password');
      return;
    }

    this.authService.login(email, password).subscribe({
      next: (response) => {
        this.error.set('');
        if (response.role === 'ROLE_ADMIN') {
          this.router.navigate(['/admin']);
        } else {
          this.router.navigate(['/']);
        }
      },
      error: () => {
        this.error.set('Invalid email or password');
      }
    });
  }

  togglePassword(): void {
    this.showPassword.update(v => !v);
  }
}

