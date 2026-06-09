import { Component, OnInit, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, ActivatedRoute, Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { ProductService } from '../../core/services/product.service';
import { CategoryService } from '../../core/services/category.service';
import { CartService } from '../../core/services/cart.service';
import { AuthService } from '../../core/services/auth.service';
@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent implements OnInit {
  products = signal<any[]>([]);
  categories = signal<any[]>([]);
  selectedCategoryId = signal<number | null>(null);
  searchQuery = signal<string>('');

  filteredProducts = computed(() => {
    let result = this.products();

    if (this.selectedCategoryId() !== null) {
      result = result.filter(p => p.categoryId === this.selectedCategoryId());
    }

    const query = this.searchQuery().toLowerCase();
    if (query) {
      result = result.filter(p =>
        p.name.toLowerCase().includes(query) ||
        p.description.toLowerCase().includes(query) ||
        p.categoryName?.toLowerCase().includes(query)
      );
    }

    return result;
  });

  constructor(
    private productService: ProductService,
    private categoryService: CategoryService,
    public cartService: CartService,
    public authService: AuthService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.productService.getAllProducts().subscribe(products => {
      this.products.set(products);
    });

    this.categoryService.getCategories().subscribe(categories => {
      this.categories.set(categories);
    });

    this.route.queryParams.subscribe(params => {
      if (params['category']) {
        this.selectedCategoryId.set(Number(params['category']));
      } else {
        this.selectedCategoryId.set(null);
      }

      if (params['search']) {
        this.searchQuery.set(params['search']);
      } else {
        this.searchQuery.set('');
      }
    });
  }

  selectCategory(categoryId: number | null): void {
    this.selectedCategoryId.set(categoryId);
    if (categoryId) {
      this.router.navigate(['/'], { queryParams: { category: categoryId } });
    } else {
      this.router.navigate(['/']);
    }
  }

  addToCart(product: any, event: Event): void {
    event.stopPropagation();
    if (product.inStock) {
      this.cartService.addToCart(product, 1);
    }
  }

  viewProduct(productId: number): void {
    this.router.navigate(['/product', productId]);
  }

  getStars(rating: number): string[] {
    const stars: string[] = [];
    const fullStars = Math.floor(rating);
    const hasHalfStar = rating % 1 >= 0.5;

    for (let i = 0; i < fullStars; i++) {
      stars.push('★');
    }
    if (hasHalfStar) {
      stars.push('☆');
    }
    while (stars.length < 5) {
      stars.push('☆');
    }
    return stars;
  }
}
