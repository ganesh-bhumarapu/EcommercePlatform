import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, ActivatedRoute, Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { ProductService } from '../../../core/services/product.service';
import { CartService } from '../../../core/services/cart.service';
import { AuthService } from '../../../core/services/auth.service';
import { Product } from '../../../core/models/product.model';

@Component({
  selector: 'app-product-detail',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './product-detail.component.html',
  styleUrl: './product-detail.component.scss'
})
export class ProductDetailComponent implements OnInit {
  product = signal<Product | undefined>(undefined);
  quantity = signal<number>(1);
  showOrderModal = signal<boolean>(false);
  orderAddress = signal<string>('');
  orderPhone = signal<string>('');
  orderPlaced = signal<boolean>(false);

  constructor(
    private productService: ProductService,
    public cartService: CartService,
    public authService: AuthService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      const productId = Number(params['id']);
      this.productService.getProductById(productId).subscribe(product => {
        this.product.set(product);
      });
    });
  }

    // Prevents increments beyond what is actually available to this user
    private getCartItemForProduct(): any | undefined {
      const prod = this.product();
      if (!prod) return undefined;
      return this.cartService.getCartItems().find(i => i.product.id === prod.id);
    }

    // Returns true if the current selected quantity has reached the max allowed
    isAtMax(): boolean {
      const prod = this.product();
      if (!prod) return true; // no product -> disable

      const cartItem = this.getCartItemForProduct();

      // if product already in cart, allowed max is current reserved qty + remaining stock
      if (cartItem) {
        const remainingOutsideCart = cartItem.product.availableQuantity ?? 0;
        const allowedMax = cartItem.quantity + remainingOutsideCart;
        return this.quantity() >= allowedMax;
      }

      // not in cart: allowed max is the product quantity returned by backend
      return this.quantity() >= (prod.quantity ?? 0);
    }

    incrementQuantity(): void {
      // Only increment if we haven't reached the allowed maximum
      if (!this.isAtMax()) {
        this.quantity.update(q => q + 1);
      } else {
        // optional: give feedback, e.g. a toast or small message
        // alert('Cannot increase quantity — no more stock available.');
      }
    }

  decrementQuantity(): void {
    if (this.quantity() > 1) {
      this.quantity.update(q => q - 1);
    }
  }

  addToCart(): void {
    const product = this.product();
    if (product && product.inStock) {
      this.cartService.addToCart(product, this.quantity());
    }
  }

  openOrderModal(): void {
    this.showOrderModal.set(true);
  }

  closeOrderModal(): void {
    this.showOrderModal.set(false);
    this.orderAddress.set('');
    this.orderPhone.set('');
  }

  placeOrder(): void {
    // Redirect to checkout
    const product = this.product();
    if (product) {
      this.cartService.addToCart(product, this.quantity());
      this.router.navigate(['/checkout']);
    }
  }

  goBack(): void {
    this.router.navigate(['/']);
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
