package com.sparta.myselectshop.controller;

import com.sparta.myselectshop.dto.*;
import com.sparta.myselectshop.security.*;
import com.sparta.myselectshop.service.*;
import lombok.*;
import org.springframework.data.domain.*;
import org.springframework.security.core.annotation.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ProductController {

    private final ProductService productService;

    @PostMapping("/products")
    public ProductResponseDto createProduct(@RequestBody ProductRequestDto requestDto, @AuthenticationPrincipal
        UserDetailsImpl userDetails){
        return productService.createProduct(requestDto,userDetails.getUser());
    }

    @PutMapping("/products/{id}")
    public ProductResponseDto updateProduct(@PathVariable Long id, @RequestBody ProductMypriceRequestDto requestDto){
        return productService.updateProduct(id, requestDto);
    }

    @GetMapping("/products")
    public Page<ProductResponseDto> getProducts(
        @RequestParam("page") int page,
        @RequestParam("size") int size,
        @RequestParam("sortBy") String sortBy,
        @RequestParam("isAsc") boolean isAsc,
        @AuthenticationPrincipal UserDetailsImpl userDetails){
        return productService.getProducts(userDetails.getUser(),
            page-1, size, sortBy,isAsc
            );
    }


}
