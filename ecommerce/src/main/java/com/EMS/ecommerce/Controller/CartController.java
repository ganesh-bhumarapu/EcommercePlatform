package com.EMS.ecommerce.Controller;

import com.EMS.ecommerce.Service.CartService;
import com.EMS.ecommerce.dto.AddToCartRequest;
import com.EMS.ecommerce.dto.CartDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public CartDTO getCart(Authentication authentication) {
        return cartService.getCart(authentication.getName());
    }

    @PostMapping("/add")
    public CartDTO addToCart(@Valid @RequestBody AddToCartRequest request,
                             Authentication authentication) {
        return cartService.addToCart(authentication.getName(), request.getProductId(), request.getQuantity());
    }

    @PutMapping("/update")
    public CartDTO updateQuantity(@Valid @RequestBody AddToCartRequest request,
                                  Authentication authentication) {
        return cartService.updateQuantity(authentication.getName(), request.getProductId(), request.getQuantity());
    }

    @DeleteMapping("/remove/{productId}")
    public CartDTO removeFromCart(@PathVariable Long productId,
                                  Authentication authentication) {
        return cartService.removeFromCart(authentication.getName(), productId);
    }

    @DeleteMapping("/clear")
    public void clearCart(Authentication authentication) {
        cartService.clearCart(authentication.getName());
    }
}
