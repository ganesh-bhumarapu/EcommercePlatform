import { Routes } from '@angular/router';

export const routes: Routes = [
  { 
    path: '', 
    loadComponent: () => import('./features/home/home.component').then(m => m.HomeComponent) 
  },
  { 
    path: 'product/:id', 
    loadComponent: () => import('./features/product/product-detail/product-detail.component').then(m => m.ProductDetailComponent) 
  },
  { 
    path: 'cart', 
    loadComponent: () => import('./features/cart/cart.component').then(m => m.CartComponent) 
  },
  { 
    path: 'checkout', 
    loadComponent: () => import('./features/checkout/checkout.component').then(m => m.CheckoutComponent) 
  },
  { 
    path: 'login', 
    loadComponent: () => import('./features/auth/login/login.component').then(m => m.LoginComponent) 
  },
  { 
    path: 'register', 
    loadComponent: () => import('./features/auth/register/register.component').then(m => m.RegisterComponent) 
  },
  { 
    path: 'admin', 
    loadComponent: () => import('./features/admin/dashboard/dashboard.component').then(m => m.DashboardComponent) 
  },
  { 
    path: 'admin/products', 
    loadComponent: () => import('./features/admin/product-management/product-management.component').then(m => m.ProductManagementComponent) 
  },
  { 
    path: 'admin/orders', 
    loadComponent: () => import('./features/admin/order-management/order-management.component').then(m => m.OrderManagementComponent) 
  },
  { 
    path: '**', 
    redirectTo: '' 
  }
];
