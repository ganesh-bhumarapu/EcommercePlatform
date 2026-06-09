package com.EMS.ecommerce.Service;

import com.EMS.ecommerce.Entity.*;
import com.EMS.ecommerce.Repository.CartRepository;
import com.EMS.ecommerce.Repository.OrderRepository;
import com.EMS.ecommerce.Repository.UserRepository;
import com.EMS.ecommerce.dto.OrderDTO;
import com.EMS.ecommerce.dto.OrderItemDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    // Services for placing orders, viewing user orders, and admin order management
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;

    // Place order from cart items then clear the cart
    @org.springframework.transaction.annotation.Transactional
    public OrderDTO placeOrder(String email, String address, String phoneNumber) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Cart is empty"));

        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        Order order = Order.builder()
                .user(user)
                .address(address)
                .phoneNumber(phoneNumber)
                .orderDate(LocalDateTime.now())
                .status(Order.OrderStatus.CONFIRMED)
                .build();

        List<OrderItem> orderItems = cart.getItems().stream()
                .map(cartItem -> OrderItem.builder()
                        .order(order)
                        .product(cartItem.getProduct())
                        .quantity(cartItem.getQuantity())
                        .price(cartItem.getProduct().getPrice())  // price snapshot
                        .build())
                .collect(Collectors.toList());

        order.setItems(orderItems);

        double total = orderItems.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
        order.setTotalAmount(total);

        Order savedOrder = orderRepository.save(order);

        // Clear the cart after order is placed
        cart.getItems().clear();
        cartRepository.save(cart);

        return toDTO(savedOrder);
    }

    public List<OrderDTO> getOrdersByUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return orderRepository.findByUserId(user.getId())
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public OrderDTO updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(Order.OrderStatus.valueOf(status));
        return toDTO(orderRepository.save(order));
    }

    private OrderDTO toDTO(Order order) {
        List<OrderItemDTO> itemDTOs = order.getItems().stream()
                .map(item -> new OrderItemDTO(
                        item.getProduct().getId(),
                        item.getProduct().getName(),
                        item.getQuantity(),
                        item.getPrice()
                ))
                .collect(Collectors.toList());

        return new OrderDTO(
                order.getId(),
                order.getUser().getId(),
                order.getUser().getName(),
                itemDTOs,
                order.getTotalAmount(),
                order.getAddress(),
                order.getPhoneNumber(),
                order.getOrderDate(),
                order.getStatus().name()
        );
    }
}
