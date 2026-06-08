import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { ProductService } from '../../../core/services/product.service';
import { CategoryService } from '../../../core/services/category.service';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-product-management',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './product-management.component.html',
  styleUrl: './product-management.component.scss'
})
export class ProductManagementComponent implements OnInit {
  products = signal<any[]>([]);
  categories = signal<any[]>([]);
  showAddModal = signal<boolean>(false);
  showEditModal = signal<boolean>(false);
  editingProduct = signal<any | null>(null);

  // New product form
  newProduct = signal({
    name: '',
    description: '',
    price: 0,
    imageUrl: '',
    categoryId: 1,
    quantity: 5,
    rating: 4.0
  });

  constructor(
    private productService: ProductService,
    public categoryService: CategoryService,
    private authService: AuthService,
    private router: Router
  ) {
    if (!this.authService.isAdmin()) {
      this.router.navigate(['/login']);
    }
  }

  ngOnInit(): void {
    this.loadProducts();
    this.categoryService.getCategories().subscribe({
      next: (cats) => this.categories.set(cats)
    });
  }

  loadProducts(): void {
    this.productService.getAllProducts().subscribe({
      next: (products) => this.products.set(products),
      error: (err) => console.error('Failed to load products', err)
    });
  }

  openAddModal(): void {
    this.newProduct.set({
      name: '',
      description: '',
      price: 0,
      imageUrl: '',
      categoryId: 1,
      quantity: 5,
      rating: 4.0
    });
    this.showAddModal.set(true);
  }

  closeAddModal(): void {
    this.showAddModal.set(false);
  }

  addProduct(): void {
    const product = this.newProduct();
    if (!product.name || !product.price) {
      alert('Please fill in all required fields');
      return;
    }

    this.productService.addProduct({
      name: product.name,
      description: product.description,
      price: product.price,
      imageUrl: product.imageUrl || 'https://via.placeholder.com/400x400?text=Product',
      categoryId: product.categoryId,
      quantity: product.quantity,
      rating: product.rating
    }).subscribe({
      next: () => {
        this.loadProducts();
        this.closeAddModal();
      },
      error: (err) => {
        console.error('Failed to add product', err);
        alert('Failed to add product');
      }
    });
  }

  openEditModal(product: any): void {
    this.editingProduct.set({ ...product });
    this.showEditModal.set(true);
  }

  closeEditModal(): void {
    this.showEditModal.set(false);
    this.editingProduct.set(null);
  }

  updateProduct(): void {
    const product = this.editingProduct();
    if (!product) return;

    this.productService.updateProduct(product.id, {
      name: product.name,
      description: product.description,
      price: product.price,
      imageUrl: product.imageUrl,
      categoryId: product.categoryId,
      quantity: product.quantity,
      rating: product.rating
    }).subscribe({
      next: () => {
        this.loadProducts();
        this.closeEditModal();
      },
      error: (err) => {
        console.error('Failed to update product', err);
        alert('Failed to update product');
      }
    });
  }

  deleteProduct(productId: number): void {
    if (confirm('Are you sure you want to delete this product?')) {
      this.productService.deleteProduct(productId).subscribe({
        next: () => this.loadProducts(),
        error: (err) => {
          console.error('Failed to delete product', err);
          alert('Failed to delete product');
        }
      });
    }
  }
}
