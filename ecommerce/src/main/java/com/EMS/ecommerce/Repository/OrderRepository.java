package com.EMS.ecommerce.Repository;

import com.EMS.ecommerce.Entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUserId(Long userId);

    long countByStatus(Order.OrderStatus status);
}
