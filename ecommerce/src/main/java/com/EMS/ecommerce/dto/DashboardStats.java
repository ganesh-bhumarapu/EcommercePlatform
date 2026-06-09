package com.EMS.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStats {
    private long totalOrders;
    private double totalRevenue;
    private long totalUsers;
    private long totalProducts;
    private long outOfStockProducts;
}
