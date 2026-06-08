package com.EMS.ecommerce.config;

import com.EMS.ecommerce.Entity.*;
import com.EMS.ecommerce.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        // ── Admin User ────────────────────────────────────────────────────────
        if (!userRepository.existsByEmail("admin@shopzone.com")) {
            userRepository.save(User.builder()
                    .name("Admin")
                    .email("admin@shopzone.com")
                    .password(passwordEncoder.encode("admin123"))
                    .role(Role.ROLE_ADMIN)
                    .build());
            System.out.println("Admin user created: admin@shopzone.com / admin123");
        }

        // Only seed if DB is empty
        if (categoryRepository.count() > 0) {
            System.out.println("Data already seeded, skipping...");
            return;
        }

        System.out.println("Seeding initial data...");

        // ── Categories ────────────────────────────────────────────────────────
        Category electronics = categoryRepository.save(Category.builder()
                .name("Electronics").description("Phones, laptops, audio and more").build());
        Category fashion = categoryRepository.save(Category.builder()
                .name("Fashion").description("Clothing, shoes and accessories").build());
        Category books = categoryRepository.save(Category.builder()
                .name("Books").description("Best sellers and classics").build());
        Category homeKitchen = categoryRepository.save(Category.builder()
                .name("Home & Kitchen").description("Appliances and cookware").build());
        Category sports = categoryRepository.save(Category.builder()
                .name("Sports & Fitness").description("Fitness gear and accessories").build());

        // ── Products — Electronics ────────────────────────────────────────────
        productRepository.save(Product.builder().name("iPhone 15 Pro")
                .description("Latest Apple smartphone with A17 Pro chip, titanium design, and advanced camera system.")
                .price(999.0).imageUrl("https://images.unsplash.com/photo-1695048133142-1a20484d2569?w=400")
                .quantity(5).inStock(true).rating(4.8).category(electronics).build());

        productRepository.save(Product.builder().name("Samsung Galaxy S24 Ultra")
                .description("Premium Android smartphone with Galaxy AI, S Pen, and 200MP camera.")
                .price(1199.0).imageUrl("https://images.unsplash.com/photo-1610945265064-0e34e5519bbf?w=400")
                .quantity(5).inStock(true).rating(4.7).category(electronics).build());

        productRepository.save(Product.builder().name("Sony WH-1000XM5")
                .description("Industry-leading noise canceling wireless headphones with 30-hour battery life.")
                .price(349.0).imageUrl("https://images.unsplash.com/photo-1618366712010-f4ae9c647dcb?w=400")
                .quantity(5).inStock(true).rating(4.9).category(electronics).build());

        productRepository.save(Product.builder().name("MacBook Pro 16\"")
                .description("Powerful laptop with M3 Pro chip, 18-hour battery, Liquid Retina XDR display.")
                .price(2499.0).imageUrl("https://images.unsplash.com/photo-1517336714731-489689fd1ca8?w=400")
                .quantity(5).inStock(true).rating(4.9).category(electronics).build());

        productRepository.save(Product.builder().name("iPad Pro 12.9\"")
                .description("Most advanced iPad with M2 chip, Liquid Retina XDR display.")
                .price(1099.0).imageUrl("https://images.unsplash.com/photo-1544244015-0df4b3ffc6b0?w=400")
                .quantity(0).inStock(false).rating(4.8).category(electronics).build());

        // ── Products — Fashion ────────────────────────────────────────────────
        productRepository.save(Product.builder().name("Nike Air Max 270")
                .description("Comfortable lifestyle shoes with Max Air unit for all-day cushioning.")
                .price(150.0).imageUrl("https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=400")
                .quantity(5).inStock(true).rating(4.5).category(fashion).build());

        productRepository.save(Product.builder().name("Levi's 501 Original Jeans")
                .description("Classic straight fit jeans with authentic vintage style.")
                .price(79.0).imageUrl("https://images.unsplash.com/photo-1542272604-787c3835535d?w=400")
                .quantity(5).inStock(true).rating(4.4).category(fashion).build());

        productRepository.save(Product.builder().name("Ray-Ban Aviator Sunglasses")
                .description("Iconic aviator style sunglasses with polarized lenses.")
                .price(169.0).imageUrl("https://images.unsplash.com/photo-1572635196237-14b3f281503f?w=400")
                .quantity(5).inStock(true).rating(4.6).category(fashion).build());

        productRepository.save(Product.builder().name("Adidas Ultraboost 23")
                .description("Premium running shoes with responsive BOOST midsole.")
                .price(190.0).imageUrl("https://images.unsplash.com/photo-1606107557195-0e29a4b5b4aa?w=400")
                .quantity(5).inStock(true).rating(4.7).category(fashion).build());

        productRepository.save(Product.builder().name("North Face Puffer Jacket")
                .description("Warm and stylish winter jacket with 700-fill goose down insulation.")
                .price(299.0).imageUrl("https://images.unsplash.com/photo-1551028719-00167b16eac5?w=400")
                .quantity(0).inStock(false).rating(4.5).category(fashion).build());

        // ── Products — Books ──────────────────────────────────────────────────
        productRepository.save(Product.builder().name("The Alchemist")
                .description("Paulo Coelho's masterpiece about following your dreams.")
                .price(15.0).imageUrl("https://images.unsplash.com/photo-1544947950-fa07a98d237f?w=400")
                .quantity(5).inStock(true).rating(4.8).category(books).build());

        productRepository.save(Product.builder().name("Atomic Habits")
                .description("James Clear's guide to building good habits and breaking bad ones.")
                .price(18.0).imageUrl("https://images.unsplash.com/photo-1589829085413-56de8ae18c73?w=400")
                .quantity(5).inStock(true).rating(4.9).category(books).build());

        productRepository.save(Product.builder().name("Think and Grow Rich")
                .description("Napoleon Hill's classic guide to success and wealth.")
                .price(14.0).imageUrl("https://images.unsplash.com/photo-1512820790803-83ca734da794?w=400")
                .quantity(5).inStock(true).rating(4.6).category(books).build());

        productRepository.save(Product.builder().name("Rich Dad Poor Dad")
                .description("Robert Kiyosaki's bestseller about financial literacy and building wealth.")
                .price(16.0).imageUrl("https://images.unsplash.com/photo-1543002588-bfa74002ed7e?w=400")
                .quantity(5).inStock(true).rating(4.7).category(books).build());

        productRepository.save(Product.builder().name("The Psychology of Money")
                .description("Morgan Housel's insights into how people think about money.")
                .price(19.0).imageUrl("https://images.unsplash.com/photo-1554224155-6726b3ff858f?w=400")
                .quantity(5).inStock(true).rating(4.8).category(books).build());

        // ── Products — Home & Kitchen ─────────────────────────────────────────
        productRepository.save(Product.builder().name("Instant Pot Duo 7-in-1")
                .description("Multi-functional electric pressure cooker with 7 cooking modes.")
                .price(89.0).imageUrl("https://images.unsplash.com/photo-1585515320310-259814833e62?w=400")
                .quantity(5).inStock(true).rating(4.7).category(homeKitchen).build());

        productRepository.save(Product.builder().name("Dyson V15 Vacuum")
                .description("Cordless vacuum with laser dust detection and powerful suction.")
                .price(749.0).imageUrl("https://images.unsplash.com/photo-1558317374-067fb5f30001?w=400")
                .quantity(5).inStock(true).rating(4.8).category(homeKitchen).build());

        productRepository.save(Product.builder().name("KitchenAid Stand Mixer")
                .description("Professional-grade stand mixer with 10 speeds for baking enthusiasts.")
                .price(379.0).imageUrl("https://images.unsplash.com/photo-1578985545062-69928b1d9587?w=400")
                .quantity(5).inStock(true).rating(4.9).category(homeKitchen).build());

        productRepository.save(Product.builder().name("Nespresso Vertuo Coffee Machine")
                .description("Premium coffee maker with centrifusion technology.")
                .price(199.0).imageUrl("https://images.unsplash.com/photo-1517668808822-9ebb02f2a0e6?w=400")
                .quantity(0).inStock(false).rating(4.6).category(homeKitchen).build());

        productRepository.save(Product.builder().name("Air Fryer XL")
                .description("Large capacity air fryer for healthy cooking with little to no oil.")
                .price(129.0).imageUrl("https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?w=400")
                .quantity(5).inStock(true).rating(4.5).category(homeKitchen).build());

        // ── Products — Sports & Fitness ───────────────────────────────────────
        productRepository.save(Product.builder().name("Apple Watch Series 9")
                .description("Advanced fitness tracking with heart rate, ECG, and blood oxygen monitoring.")
                .price(399.0).imageUrl("https://images.unsplash.com/photo-1434493789847-2f02dc6ca35d?w=400")
                .quantity(5).inStock(true).rating(4.8).category(sports).build());

        productRepository.save(Product.builder().name("Yoga Mat Premium")
                .description("Extra thick non-slip yoga mat for yoga, pilates, and floor exercises.")
                .price(45.0).imageUrl("https://images.unsplash.com/photo-1601925260368-ae2f83cf8b7f?w=400")
                .quantity(5).inStock(true).rating(4.5).category(sports).build());

        productRepository.save(Product.builder().name("Adjustable Dumbbell Set")
                .description("Space-saving adjustable dumbbells from 5 to 52.5 lbs.")
                .price(349.0).imageUrl("https://images.unsplash.com/photo-1534438327276-14e5300c3a48?w=400")
                .quantity(5).inStock(true).rating(4.7).category(sports).build());

        productRepository.save(Product.builder().name("Resistance Bands Set")
                .description("Complete set of 5 resistance bands for full body workout.")
                .price(29.0).imageUrl("https://images.unsplash.com/photo-1598289431512-b97b0917affc?w=400")
                .quantity(5).inStock(true).rating(4.4).category(sports).build());

        productRepository.save(Product.builder().name("Fitbit Charge 6")
                .description("Advanced fitness tracker with GPS, heart rate and sleep tracking.")
                .price(159.0).imageUrl("https://images.unsplash.com/photo-1575311373937-040b8e1fd5b6?w=400")
                .quantity(5).inStock(true).rating(4.6).category(sports).build());



        System.out.println("Seeding complete! 5 categories, 25 products, 1 admin user.");
    }
}
