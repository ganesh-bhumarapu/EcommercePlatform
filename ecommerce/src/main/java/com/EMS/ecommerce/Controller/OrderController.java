package com.EMS.ecommerce.Controller;

import com.EMS.ecommerce.Service.OrderService;
import com.EMS.ecommerce.dto.CreateOrderRequest;
import com.EMS.ecommerce.dto.OrderDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public OrderDTO placeOrder(@Valid @RequestBody CreateOrderRequest request,
                               Authentication authentication) {
        return orderService.placeOrder(authentication.getName(), request.getAddress(), request.getPhoneNumber());
    }

    @GetMapping("/my-orders")
    public List<OrderDTO> getMyOrders(Authentication authentication) {
        return orderService.getOrdersByUser(authentication.getName());
    }
}
