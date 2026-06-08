package com.EMS.ecommerce.Service;

import com.EMS.ecommerce.Entity.Category;
import com.EMS.ecommerce.Entity.Product;
import com.EMS.ecommerce.Repository.CategoryRepository;
import com.EMS.ecommerce.Repository.ProductRepository;
import com.EMS.ecommerce.dto.ProductCreateDTO;
import com.EMS.ecommerce.dto.ProductDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return toDTO(product);
    }

    public List<ProductDTO> getProductsByCategory(Long categoryId) {
        return productRepository.findByCategoryId(categoryId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<ProductDTO> searchProducts(String query) {
        return productRepository.findByNameContainingIgnoreCase(query)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // Returns first 8 products as featured
    public List<ProductDTO> getFeaturedProducts() {
        return productRepository.findAll(PageRequest.of(0, 8))
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public ProductDTO addProduct(ProductCreateDTO request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .imageUrl(request.getImageUrl())
                .quantity(request.getQuantity())
                .inStock(request.getQuantity() > 0)
                .rating(request.getRating() != null ? request.getRating() : 0.0)
                .category(category)
                .build();

        return toDTO(productRepository.save(product));
    }

    public ProductDTO updateProduct(Long id, ProductCreateDTO request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setImageUrl(request.getImageUrl());
        product.setQuantity(request.getQuantity());
        product.setInStock(request.getQuantity() > 0);
        product.setRating(request.getRating() != null ? request.getRating() : product.getRating());
        product.setCategory(category);

        return toDTO(productRepository.save(product));
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    private ProductDTO toDTO(Product product) {
        return new ProductDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getImageUrl(),
                product.getQuantity(),
                product.getInStock(),
                product.getRating(),
                product.getCategory().getId(),
                product.getCategory().getName()
        );
    }
}
