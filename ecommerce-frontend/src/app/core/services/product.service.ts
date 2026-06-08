import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  private readonly API_URL = 'http://localhost:8081/api';

  constructor(private http: HttpClient) {}

  getAllProducts(): Observable<any[]> {
    return this.http.get<any[]>(`${this.API_URL}/products`);
  }

  getProductById(id: number): Observable<any> {
    return this.http.get<any>(`${this.API_URL}/products/${id}`);
  }

  getFeaturedProducts(): Observable<any[]> {
    return this.http.get<any[]>(`${this.API_URL}/products/featured`);
  }

  searchProducts(query: string): Observable<any[]> {
    const params = new HttpParams().set('q', query);
    return this.http.get<any[]>(`${this.API_URL}/products/search`, { params });
  }

  getProductsByCategory(categoryId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.API_URL}/products/category/${categoryId}`);
  }

  // Admin: add product
  addProduct(product: any): Observable<any> {
    return this.http.post<any>(`${this.API_URL}/admin/products`, product);
  }

  // Admin: update product
  updateProduct(id: number, product: any): Observable<any> {
    return this.http.put<any>(`${this.API_URL}/admin/products/${id}`, product);
  }

  // Admin: delete product
  deleteProduct(id: number): Observable<any> {
    return this.http.delete(`${this.API_URL}/admin/products/${id}`, { responseType: 'text' });
  }
}
