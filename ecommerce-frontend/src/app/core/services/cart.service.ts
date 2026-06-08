import { environment } from '../../../environments/environment';
import { Injectable, signal, computed } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class CartService {
  private readonly API_URL = environment.apiUrl + '/api/cart';
  private cartItems = signal<any[]>([]);

  constructor(private http: HttpClient) {
    this.loadCart();
  }

  // Fetch cart from backend on load
  loadCart(): void {
    // We only load if logged in, but the interceptor handles token.
    // If not logged in, this might fail, so we catch error.
    this.http.get<any>(this.API_URL).subscribe({
      next: (cart) => {
        if (cart && cart.items) {
          // Map backend items to frontend format: { product: { ... }, quantity: ... }
          const mappedItems = cart.items.map((item: any) => ({
            id: item.cartItemId,
            product: {
              id: item.productId,
              name: item.productName,
              price: item.price,
              imageUrl: item.imageUrl,
              availableQuantity: item.availableQuantity ?? 0
            },
            quantity: item.quantity
          }));
          this.cartItems.set(mappedItems);
        } else {
          this.cartItems.set([]);
        }
      },
      error: () => {
        this.cartItems.set([]);
      }
    });
  }

  // Computed values
  cartCount = computed(() =>
    this.cartItems().reduce((count, item) => count + item.quantity, 0)
  );

  cartTotal = computed(() =>
    this.cartItems().reduce((total, item) => total + (item.product.price * item.quantity), 0)
  );

  getCartItems(): any[] {
    return this.cartItems();
  }

  isInCart(productId: number): boolean {
    return this.cartItems().some(item => item.product.id === productId);
  }

  addToCart(product: any, quantity: number = 1): void {
    this.http.post<any>(`${this.API_URL}/add`, { productId: product.id, quantity }).subscribe({
      next: () => this.loadCart(),
      error: (err) => console.error('Failed to add to cart', err)
    });
  }

  removeFromCart(productId: number): void {
    this.http.delete<any>(`${this.API_URL}/remove/${productId}`).subscribe({
      next: () => this.loadCart(),
      error: (err) => console.error('Failed to remove from cart', err)
    });
  }

  updateQuantity(productId: number, quantity: number): void {
    if (quantity <= 0) {
      this.removeFromCart(productId);
      return;
    }
    this.http.put<any>(`${this.API_URL}/update`, { productId, quantity }).subscribe({
      next: () => this.loadCart(),
      error: (err) => console.error('Failed to update quantity', err)
    });
  }

  incrementQuantity(productId: number): void {
    const item = this.cartItems().find(i => i.product.id === productId);
    if (!item) return;

    // remaining stock excluding this cart's reservation
    const avail = item.product.availableQuantity ?? 0;

    // if no remaining stock outside this cart, don't try incrementing
    if (avail <= 0) {
      // optionally: show a toast/alert to the user
      return;
    }

//     // optional per-item cap enforcement
//     if (item.quantity >= this.MAX_PER_ITEM) {
//       // optionally: show "max per-item limit reached"
//       return;
//     }

    this.updateQuantity(productId, item.quantity + 1);
  }

  decrementQuantity(productId: number): void {
    const item = this.cartItems().find(i => i.product.id === productId);
    if (item && item.quantity > 1) {
      this.updateQuantity(productId, item.quantity - 1);
    } else if (item) {
      this.removeFromCart(productId);
    }
  }

  clearCart(): void {
    this.http.delete(`${this.API_URL}/clear`, { responseType: 'text' }).subscribe({
      next: () => this.cartItems.set([]),
      error: (err) => console.error('Failed to clear cart', err)
    });
  }
}
