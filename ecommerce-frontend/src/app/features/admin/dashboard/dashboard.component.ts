import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { AdminService } from '../../../core/services/admin.service';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss'
})
export class DashboardComponent implements OnInit {
  stats = signal<any>({
    totalProducts: 0,
    totalOrders: 0,
    totalRevenue: 0,
    totalUsers: 0
  });

  constructor(
    private adminService: AdminService,
    public authService: AuthService,
    private router: Router
  ) {
    if (!this.authService.isAdmin()) {
      this.router.navigate(['/login']);
    }
  }

  ngOnInit(): void {
    this.adminService.getDashboardStats().subscribe({
      next: (data) => this.stats.set(data),
      error: (err) => console.error('Failed to load stats', err)
    });
  }

  get totalProducts(): number {
    return this.stats().totalProducts;
  }

  get totalOrders(): number {
    return this.stats().totalOrders;
  }

  get totalRevenue(): number {
    return this.stats().totalRevenue;
  }

  get outOfStockProducts(): number {
    return 0; // The backend stats don't return this yet, so we just return 0 for now.
  }
}
