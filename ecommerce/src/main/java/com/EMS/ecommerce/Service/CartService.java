package com.EMS.ecommerce.Service;

import com.EMS.ecommerce.Entity.Cart;
import com.EMS.ecommerce.Entity.CartItem;
import com.EMS.ecommerce.Entity.Product;
import com.EMS.ecommerce.Entity.User;
import com.EMS.ecommerce.Repository.CartRepository;
import com.EMS.ecommerce.Repository.ProductRepository;
import com.EMS.ecommerce.Repository.UserRepository;
import com.EMS.ecommerce.dto.CartDTO;
import com.EMS.ecommerce.dto.CartItemDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    // Gets cart for a user — creates one if it doesn't exist
    public CartDTO getCart(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    Cart newCart = Cart.builder().user(user).build();
                    return cartRepository.save(newCart);
                });

        return toDTO(cart);
    }

//    public CartDTO addToCart(String email, Long productId, Integer quantity) {
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        Product product = productRepository.findById(productId)
//                .orElseThrow(() -> new RuntimeException("Product not found"));
//
//        Cart cart = cartRepository.findByUserId(user.getId())
//                .orElseGet(() -> {
//                    Cart newCart = Cart.builder().user(user).build();
//                    return cartRepository.save(newCart);
//                });
//
//        // If product already in cart, increase quantity
//        Optional<CartItem> existingItem = cart.getItems().stream()
//                .filter(item -> item.getProduct().getId().equals(productId))
//                .findFirst();
//
//        if (existingItem.isPresent()) {
//            existingItem.get().setQuantity(existingItem.get().getQuantity() + quantity);
//        } else {
//            CartItem newItem = CartItem.builder()
//                    .cart(cart)
//                    .product(product)
//                    .quantity(quantity)
//                    .build();
//            cart.getItems().add(newItem);
//        }
//
//        return toDTO(cartRepository.save(cart));
//    }
    // annotate the method
    @Transactional
    public CartDTO addToCart(String email, Long productId, Integer quantity) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (quantity <= 0) {
            throw new RuntimeException("Quantity must be positive");
        }

        // check availability
        if (product.getQuantity() == null || product.getQuantity() < quantity) {
            throw new RuntimeException("Insufficient stock");
        }

        // reserve stock
        product.setQuantity(product.getQuantity() - quantity);
        product.setInStock(product.getQuantity() > 0);
        productRepository.save(product);

        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    Cart newCart = Cart.builder().user(user).build();
                    return cartRepository.save(newCart);
                });

        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();

        if (existingItem.isPresent()) {
            // increase cart item quantity (we already reserved the extra qty above)
            existingItem.get().setQuantity(existingItem.get().getQuantity() + quantity);
        } else {
            CartItem newItem = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(quantity)
                    .build();
            cart.getItems().add(newItem);
        }

        return toDTO(cartRepository.save(cart));
    }

//    public CartDTO updateQuantity(String email, Long productId, Integer quantity) {
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        Cart cart = cartRepository.findByUserId(user.getId())
//                .orElseThrow(() -> new RuntimeException("Cart not found"));
//
//        if (quantity <= 0) {
//            return removeFromCart(email, productId);
//        }
//
//        cart.getItems().stream()
//                .filter(item -> item.getProduct().getId().equals(productId))
//                .findFirst()
//                .ifPresent(item -> item.setQuantity(quantity));
//
//        return toDTO(cartRepository.save(cart));
//    }
    @Transactional
    public CartDTO updateQuantity(String email, Long productId, Integer quantity) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        if (quantity <= 0) {
            return removeFromCart(email, productId);
        }

        // find cart item
        Optional<CartItem> optionalItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();

        if (optionalItem.isEmpty()) {
            throw new RuntimeException("Cart item not found");
        }

        CartItem item = optionalItem.get();
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        int oldQty = item.getQuantity();
        int delta = quantity - oldQty;

        if (delta > 0) {
            // need to reserve more
            if (product.getQuantity() == null || product.getQuantity() < delta) {
                throw new RuntimeException("Insufficient stock to increase quantity");
            }
            product.setQuantity(product.getQuantity() - delta);
        } else if (delta < 0) {
            // release some back to stock
            product.setQuantity(product.getQuantity() - delta); // delta is negative
        }

        product.setInStock(product.getQuantity() > 0);
        productRepository.save(product);

        item.setQuantity(quantity);
        return toDTO(cartRepository.save(cart));
    }

//    public CartDTO removeFromCart(String email, Long productId) {
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        Cart cart = cartRepository.findByUserId(user.getId())
//                .orElseThrow(() -> new RuntimeException("Cart not found"));
//
//        cart.getItems().removeIf(item -> item.getProduct().getId().equals(productId));
//
//        return toDTO(cartRepository.save(cart));
//    }
    @Transactional
    public CartDTO removeFromCart(String email, Long productId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        // find the item to remove and restore stock
        Optional<CartItem> optionalItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();

        if (optionalItem.isPresent()) {
            CartItem item = optionalItem.get();
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            // restore reserved quantity
            Integer restoreQty = item.getQuantity();
            product.setQuantity((product.getQuantity() == null ? 0 : product.getQuantity()) + restoreQty);
            product.setInStock(product.getQuantity() > 0);
            productRepository.save(product);

            cart.getItems().remove(item);
        }

        return toDTO(cartRepository.save(cart));
    }

//    public void clearCart(String email) {
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        Cart cart = cartRepository.findByUserId(user.getId())
//                .orElseThrow(() -> new RuntimeException("Cart not found"));
//
//        cart.getItems().clear();
//        cartRepository.save(cart);
//    }
    @Transactional
    public void clearCart(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        // restore stock for all items
        for (CartItem item : cart.getItems()) {
            Product product = productRepository.findById(item.getProduct().getId())
                    .orElse(null);
            if (product != null) {
                product.setQuantity((product.getQuantity() == null ? 0 : product.getQuantity()) + item.getQuantity());
                product.setInStock(product.getQuantity() > 0);
                productRepository.save(product);
            }
        }

        cart.getItems().clear();
        cartRepository.save(cart);
    }

    private CartDTO toDTO(Cart cart) {
        List<CartItemDTO> itemDTOs = cart.getItems().stream()
                .map(item -> new CartItemDTO(
                        item.getId(),
                        item.getProduct().getId(),
                        item.getProduct().getName(),
                        item.getProduct().getImageUrl(),
                        item.getProduct().getPrice(),
                        item.getQuantity(),
                        item.getProduct().getQuantity()
                ))
                .collect(Collectors.toList());

        double total = cart.getItems().stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();

        return new CartDTO(cart.getId(), itemDTOs, total);
    }
}
