package com.example.project.controllers;

import com.example.project.dtos.request.CreateProductDto;
import com.example.project.models.Product;
import com.example.project.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ProductController {
    private final ProductService productService;

    @PostMapping(path = "/product")
    public ResponseEntity<String> createProduct(@RequestBody CreateProductDto createProductDto) {
        Product product = productService.save((createProductDto));
        return new ResponseEntity<String>("Product created success!", HttpStatus.CREATED);
    }

    @GetMapping(path = "/products")
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.findAll();
        return new ResponseEntity<List<Product>>(products, HttpStatus.OK);
    }

    @GetMapping(path = "/product/{id}")
    public ResponseEntity<?> getProductById(@PathVariable UUID id) {
        return productService.findById(id)
                .map(product -> new ResponseEntity<Product>(product, HttpStatus.OK))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
    }

    @DeleteMapping(path = "/product/{id}")
    public ResponseEntity<String> deleteProductById(@PathVariable UUID id) {
        return productService.findById(id).map(product -> {
            productService.delete(id);
            return new ResponseEntity<>("Product deleted successfully", HttpStatus.OK);
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
    }


    @PutMapping(path = "/product/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable UUID id,
                                                @RequestBody  CreateProductDto createProductDto) {
        if (!productService.isExists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(productService.update(id, createProductDto), HttpStatus.OK);
    }


}