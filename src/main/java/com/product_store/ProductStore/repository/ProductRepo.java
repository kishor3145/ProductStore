package com.product_store.ProductStore.repository;

import com.product_store.ProductStore.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepo extends JpaRepository<Product, Integer> {
}
