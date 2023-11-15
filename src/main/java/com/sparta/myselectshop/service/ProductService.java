package com.sparta.myselectshop.service;

import com.sparta.myselectshop.dto.*;
import com.sparta.myselectshop.entity.*;
import com.sparta.myselectshop.repository.*;
import lombok.*;
import org.springframework.stereotype.*;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public ProductResponseDto createProduct(ProductRequestDto requestDto) {
        Product product = productRepository.save(new Product(requestDto));
        return new ProductResponseDto(product);
    }
}
