package com.EMS.ecommerce.Service;

import com.EMS.ecommerce.Repository.OrderRepository;
import com.EMS.ecommerce.Repository.ProductRepository;
import com.EMS.ecommerce.Repository.UserRepository;
import com.EMS.ecommerce.dto.DashboardStats;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public DashboardStats getDashboardStats() {
        long totalOrders = orderRepository.count();
        long totalProducts = productRepository.count();
        long totalUsers = userRepository.count();

        double totalRevenue = orderRepository.findAll()
                .stream()
                .mapToDouble(order -> order.getTotalAmount() != null ? order.getTotalAmount() : 0)
                .sum();

        long outOfStockProducts = productRepository.countByInStockFalse();

        return new DashboardStats(totalOrders, totalRevenue, totalUsers, totalProducts, outOfStockProducts);
    }
}
