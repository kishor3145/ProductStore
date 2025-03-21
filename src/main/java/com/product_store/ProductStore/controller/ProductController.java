package com.product_store.ProductStore.controller;

import com.product_store.ProductStore.models.Product;
import com.product_store.ProductStore.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductRepo repo;

    @GetMapping({"", "/"})
    public String showProductList(Model model) {
        List<Product> productList = repo.findAll(Sort.by(Sort.Direction.ASC,"id"));
        System.out.println("Products: " + productList); // Debugging: Print products to console

        model.addAttribute("products", productList);
        return "products/index";
    }
}