package com.EMS.ecommerce.Entity;

import jakarta.persistence.*;
import lombok.*;
import jakarta.persistence.Version;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(length = 1000)
    private String description;

    private Double price;
    private String imageUrl;
    private Integer quantity;
    private Boolean inStock;
    private Double rating;
//    @Version
//    private Integer version;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}
