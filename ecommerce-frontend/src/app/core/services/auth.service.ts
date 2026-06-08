import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { User } from '../models/user.model';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly API_URL = 'http://localhost:8081/api/auth';

  private currentUser = signal<User | null>(null);
  isLoggedIn = signal<boolean>(false);
  isAdmin = signal<boolean>(false);
  user = this.currentUser.asReadonly();

  constructor(private http: HttpClient) {
    this.checkStoredUser();
  }

  private checkStoredUser(): void {
    const storedUser = localStorage.getItem('user');
    if (storedUser) {
      const user = JSON.parse(storedUser);
      this.currentUser.set(user);
      this.isLoggedIn.set(true);
      this.isAdmin.set(user.role === 'ROLE_ADMIN');
    }
  }

  login(email: string, password: string): Observable<any> {
    return this.http.post<any>(`${this.API_URL}/login`, { email, password }).pipe(
      tap(response => {
        const user: User = {
          id: 0,
          name: response.name,
          email: email,
          role: response.role,
          token: response.token
        };
        this.setUser(user);
      })
    );
  }

//   register(name: string, email: string, password: string): Observable<any> {
//     return this.http.post(`${this.API_URL}/register`, { name, email, password }, { responseType: 'text' });
//   }
  register(name: string, email: string, password: string): Observable<any> {
    return this.http.post<any>(`${this.API_URL}/register`, { name, email, password });
  }

  private setUser(user: User): void {
    localStorage.setItem('user', JSON.stringify(user));
    this.currentUser.set(user);
    this.isLoggedIn.set(true);
    this.isAdmin.set(user.role === 'ROLE_ADMIN');
  }

  logout(): void {
    localStorage.removeItem('user');
    this.currentUser.set(null);
    this.isLoggedIn.set(false);
    this.isAdmin.set(false);
  }

  getToken(): string | null {
    const user = this.currentUser();
    return user ? user.token || null : null;
  }

  getCurrentUser(): User | null {
    return this.currentUser();
  }
}
