package com.sparta.myselectshop.controller;

import com.sparta.myselectshop.dto.*;
import com.sparta.myselectshop.service.*;
import java.util.*;
import lombok.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ProductController {

    private final ProductService productService;

    @PostMapping("/products")
    public ProductResponseDto createProduct(@RequestBody ProductRequestDto requestDto){
        return productService.createProduct(requestDto);
    }
    @PutMapping("/products/{id}")
    public ProductResponseDto updateProduct(@PathVariable Long id, @RequestBody ProductMypriceRequestDto requestDto){
        return productService.updateProduct(id, requestDto);
    }
    @GetMapping("/products")
    public List<ProductResponseDto> getProducts(){
        return productService.getProducts();
    }
}
