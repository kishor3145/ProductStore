package com.product_store.ProductStore.controller;

import com.product_store.ProductStore.models.Product;
import com.product_store.ProductStore.models.ProductDTO;
import com.product_store.ProductStore.repository.ProductRepo;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.*;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductRepo repo;

    @GetMapping({"", "/"})
    public String showProductList(Model model) {
        List<Product> productList = repo.findAll(Sort.by(Sort.Direction.ASC, "id"));
        model.addAttribute("products", productList);
        return "products/index";
    }

    @GetMapping("/create")
    public String showCreatePage(Model model) {
        model.addAttribute("productDto", new ProductDTO());
        return "products/CreateProduct";
    }

    @PostMapping("/create")
    public String createProduct(@Valid @ModelAttribute("productDto") ProductDTO productDTO,
                                BindingResult result,
                                Model model) {
        // Validate image file
        if (productDTO.getImageFile().isEmpty()) {
            result.addError(new FieldError("productDto", "imageFile", "The image file is required"));
        }

        // Return to the form if there are validation errors
        if (result.hasErrors()) {
            return "products/CreateProduct";
        }

        // Save Image File
        MultipartFile image = productDTO.getImageFile();
        Date createdAt = new Date();
        String storageFileName = createdAt.getTime() + "_" + image.getOriginalFilename();

        try {
            String uploadDir = "public/images/";
            Path uploadPath = Paths.get(uploadDir);

            // Create the upload directory if it doesn't exist
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Save the file to the upload directory
            try (InputStream inputStream = image.getInputStream()) {
                Files.copy(inputStream, Paths.get(uploadDir + storageFileName), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
            model.addAttribute("error", "Failed to upload image. Please try again.");
            return "products/CreateProduct";
        }

        // Convert DTO to Entity
        Product product = new Product();
        product.setName(productDTO.getName());
        product.setBrand(productDTO.getBrand());
        product.setCategory(productDTO.getCategory());
        product.setPrice(productDTO.getPrice());
        product.setDescription(productDTO.getDescription());
        product.setCreatedAt(createdAt);
        product.setImageFileName(storageFileName); // Set stored image path

        // Save to DB
        try {
            repo.save(product);
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
            model.addAttribute("error", "Failed to save product. Please try again.");
            return "products/CreateProduct";
        }

        return "redirect:/products";


    }
    @GetMapping("/edit/{id}")
    public String editPage(Model model, @PathVariable int id){
    try{
        Product product = repo.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        ProductDTO productDTO=new ProductDTO();
        productDTO.setName(product.getName());
        productDTO.setBrand(product.getBrand());
        productDTO.setCategory(product.getCategory());
        productDTO.setPrice(product.getPrice());
        productDTO.setDescription(product.getDescription());
        model.addAttribute("productDto", productDTO);
        model.addAttribute("product",product);

//        model.addAttribute("product", product);
        return "products/EditProduct";

    }catch (Exception e){
        System.out.println("Exception:"+e.getMessage());
        return "redirect:/products";
    }
    }
    @PostMapping("/edit/{id}")
    public String updateProduct(@PathVariable int id,
                                @Valid @ModelAttribute("productDto") ProductDTO productDto,
                                BindingResult result,
                                Model model) {

        if (result.hasErrors()) {
            // Re-populate product if validation fails
            Product product = repo.findById(id).orElseThrow();
            model.addAttribute("product", product);
            return "products/EditProduct";
        }

        // Handle file upload and update logic
        Product product = repo.findById(id).orElseThrow();
        product.setName(productDto.getName());
        product.setBrand(productDto.getBrand());
        product.setCategory(productDto.getCategory());
        product.setPrice(productDto.getPrice());
        product.setDescription(productDto.getDescription());

        if (!productDto.getImageFile().isEmpty()) {
            // Handle new image upload (similar to create method)
            // ... (your image upload logic here) ...
        }

        repo.save(product);
        return "redirect:/products";
    }
}
