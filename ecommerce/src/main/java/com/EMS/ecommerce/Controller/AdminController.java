//package com.EMS.ecommerce.Controller;
//
//import com.EMS.ecommerce.Service.AdminService;
//import com.EMS.ecommerce.Service.AuthService;
//import com.EMS.ecommerce.Service.OrderService;
//import com.EMS.ecommerce.Service.ProductService;
//import com.EMS.ecommerce.dto.*;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/admin")
//@RequiredArgsConstructor
//public class AdminController {
//
//    private final AdminService adminService;
//    private final AuthService authService;
//    private final ProductService productService;
//    private final OrderService orderService;
//
//    // ── Dashboard ────────────────────────────────────────────────────────────
//    @GetMapping("/dashboard")
//    public DashboardStats getDashboardStats() {
//        return adminService.getDashboardStats();
//    }
//
//    // ── User Management ──────────────────────────────────────────────────────
//    @PostMapping("/create-user")
//    public String createUser(@Valid @RequestBody CreateUserRequest request) {
//        return authService.createUser(request);
//    }
//
//    // ── Product Management ───────────────────────────────────────────────────
//    @PostMapping("/products")
//    public ProductDTO addProduct(@Valid @RequestBody ProductCreateDTO request) {
//        return productService.addProduct(request);
//    }
//
//    @PutMapping("/products/{id}")
//    public ProductDTO updateProduct(@PathVariable Long id,
//                                    @Valid @RequestBody ProductCreateDTO request) {
//        return productService.updateProduct(id, request);
//    }
//
//    @DeleteMapping("/products/{id}")
//    public String deleteProduct(@PathVariable Long id) {
//        productService.deleteProduct(id);
//        return "Product deleted successfully";
//    }
//
//    // ── Order Management ─────────────────────────────────────────────────────
//    @GetMapping("/orders")
//    public List<OrderDTO> getAllOrders() {
//        return orderService.getAllOrders();
//    }
//
//    @PutMapping("/orders/{id}/status")
//    public OrderDTO updateOrderStatus(@PathVariable Long id,
//                                      @RequestParam String status) {
//        return orderService.updateOrderStatus(id, status);
//    }
//}
//

package com.EMS.ecommerce.Controller;

import com.EMS.ecommerce.Service.AdminService;
import com.EMS.ecommerce.Service.AuthService;
import com.EMS.ecommerce.Service.OrderService;
import com.EMS.ecommerce.Service.ProductService;
import com.EMS.ecommerce.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final AuthService authService;
    private final ProductService productService;
    private final OrderService orderService;

    // ── Dashboard ────────────────────────────────────────────────────────────
    @GetMapping("/dashboard")
    public DashboardStats getDashboardStats() {
        return adminService.getDashboardStats();
    }

    // ── User Management ──────────────────────────────────────────────────────
    @PostMapping("/create-user")
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
        UserResponse userResp = authService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(userResp);
    }

    // ── Product Management ───────────────────────────────────────────────────
    @PostMapping("/products")
    public ProductDTO addProduct(@Valid @RequestBody ProductCreateDTO request) {
        return productService.addProduct(request);
    }

    @PutMapping("/products/{id}")
    public ProductDTO updateProduct(@PathVariable Long id,
                                    @Valid @RequestBody ProductCreateDTO request) {
        return productService.updateProduct(id, request);
    }

    @DeleteMapping("/products/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "Product deleted successfully";
    }

    // ── Order Management ─────────────────────────────────────────────────────
    @GetMapping("/orders")
    public List<OrderDTO> getAllOrders() {
        return orderService.getAllOrders();
    }

    @PutMapping("/orders/{id}/status")
    public OrderDTO updateOrderStatus(@PathVariable Long id,
                                      @RequestParam String status) {
        return orderService.updateOrderStatus(id, status);
    }
}