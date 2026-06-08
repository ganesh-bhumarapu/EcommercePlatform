package com.EMS.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductCreateDTO {

    @NotBlank(message = "Name is required")
    private String name;

    private String description;

    @NotNull(message = "Price is required")
    private Double price;

    private String imageUrl;

    @NotNull(message = "Quantity is required")
    private Integer quantity;

    private Double rating;

    @NotNull(message = "Category is required")
    private Long categoryId;
}
