import { environment } from '../../../environments/environment';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AdminService {
  private readonly API_URL = environment.apiUrl + '/api/admin';

  constructor(private http: HttpClient) {}

  getDashboardStats(): Observable<any> {
    return this.http.get<any>(`${this.API_URL}/dashboard`);
  }
}
