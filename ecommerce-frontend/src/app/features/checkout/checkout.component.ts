import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CartService } from '../../core/services/cart.service';
import { OrderService } from '../../core/services/order.service';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-checkout',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './checkout.component.html',
  styleUrl: './checkout.component.scss'
})
export class CheckoutComponent {
  address = signal<string>('');
  phoneNumber = signal<string>('');
  orderPlaced = signal<boolean>(false);
  orderError = signal<string>('');

  constructor(
    public cartService: CartService,
    private orderService: OrderService,
    private authService: AuthService,
    private router: Router
  ) {}

  placeOrder(): void {
    const address = this.address().trim();
    const phone = this.phoneNumber().trim();

    if (!address) {
      this.orderError.set('Please enter your delivery address');
      return;
    }

    if (!phone || phone.length < 10) {
      this.orderError.set('Please enter a valid phone number');
      return;
    }

    const cartItems = this.cartService.getCartItems();
    if (cartItems.length === 0) {
      this.orderError.set('Your cart is empty');
      return;
    }

    this.orderService.placeOrder(address, phone).subscribe({
      next: (order) => {
        this.cartService.loadCart(); // Backend clears cart, frontend just reloads it
        this.orderPlaced.set(true);
        this.orderError.set('');
      },
      error: (err) => {
        this.orderError.set('Failed to place order. Please try again.');
        console.error('Order error', err);
      }
    });
  }

  goHome(): void {
    this.router.navigate(['/']);
  }

  goToCart(): void {
    this.router.navigate(['/cart']);
  }
}
