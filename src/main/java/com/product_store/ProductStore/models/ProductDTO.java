package com.product_store.ProductStore.models;

import jakarta.validation.constraints.NotEmpty;

public class ProductDTO {
    @NotEmpty(message = "name is required")
    private String name;
    @NotEmpty(message = "brand name is required")
    private String brand;
    @NotEmpty(message = "category is required")
    private String category;


}
