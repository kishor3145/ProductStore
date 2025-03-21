package com.product_store.ProductStore.models;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public class ProductDTO {
    @NotEmpty(message = "name is required")
    private String name;
    @NotEmpty(message = "brand name is required")
    private String brand;
    @NotEmpty(message = "category is required")
    private String category;

    @Min(0)
    private double price;

    @Size(min = 10, message ="the description should be at least 10 characters")
    @Size(max = 200,message = "the description should not be exeeded more than 2000 characters")
    private String description;

    @NotNull(message = "Image is required")

    private MultipartFile imageFile;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MultipartFile getImageFile() {
        return imageFile;
    }

    public void setImageFile(MultipartFile imageFile) {
        this.imageFile = imageFile;
    }
}
