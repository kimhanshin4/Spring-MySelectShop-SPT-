package com.sparta.myselectshop.service;

import com.sparta.myselectshop.dto.*;
import com.sparta.myselectshop.entity.*;
import com.sparta.myselectshop.naver.dto.*;
import com.sparta.myselectshop.repository.*;
import java.util.*;
import lombok.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    public static final int MIN_MY_PRICE = 100;

    public ProductResponseDto createProduct(ProductRequestDto requestDto, User user) {
        Product product = productRepository.save(new Product(requestDto,user));
        return new ProductResponseDto(product);
    }
@Transactional
    public ProductResponseDto updateProduct(Long id, ProductMypriceRequestDto requestDto) {
        int myprice = requestDto.getMyprice();
        if (myprice < MIN_MY_PRICE) {
            throw new  IllegalArgumentException("유효하지 않은 관심 가격 감지되었습니다. 최소 " + MIN_MY_PRICE + "원 이상으로 설정하세요.");
        }
        Product product = productRepository.findById(id).orElseThrow(()->
            new NullPointerException("해당 상품을 찾을 수가 없어버립니다.")
        );
        product.update(requestDto);
        return new ProductResponseDto(product);
    }

    @Transactional(readOnly = true)
    public Page<ProductResponseDto> getProducts(User user, int page, int size, String sortBy,
        boolean isAsc) {
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page,size,sort);

        UserRoleEnum userRoleEnum = user.getRole();

        Page<Product> productList;

        if(userRoleEnum == UserRoleEnum.USER) {
            productList = productRepository.findAllByUser(user, pageable);
        } else {
            productList = productRepository.findAll(pageable);
        }
        return productList.map(ProductResponseDto::new);
    }

    @Transactional
    public void updateBySearch(Long id, ItemDto itemDto) {
        Product product = productRepository.findById(id).orElseThrow(()->
            new NullPointerException("해당 상품은 지금 없어요!")
        );
        product.updateByItemDto(itemDto);
    }


}
