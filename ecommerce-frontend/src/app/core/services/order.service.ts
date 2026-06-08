import { environment } from '../../../environments/environment';
import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class OrderService {
  private readonly API_URL = environment.apiUrl + '/api';

  constructor(private http: HttpClient) {}

  placeOrder(address: string, phoneNumber: string): Observable<any> {
    return this.http.post<any>(`${this.API_URL}/orders`, { address, phoneNumber });
  }

  getMyOrders(): Observable<any[]> {
    return this.http.get<any[]>(`${this.API_URL}/orders/my-orders`);
  }

  // Admin: get all orders
  getAllOrders(): Observable<any[]> {
    return this.http.get<any[]>(`${this.API_URL}/admin/orders`);
  }

  // Admin: update order status
  updateOrderStatus(orderId: number, status: string): Observable<any> {
    const params = new HttpParams().set('status', status);
    return this.http.put<any>(`${this.API_URL}/admin/orders/${orderId}/status`, null, { params });
  }
}
