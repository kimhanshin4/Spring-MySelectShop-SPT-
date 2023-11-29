package com.sparta.myselectshop.service;

import com.sparta.myselectshop.dto.*;
import com.sparta.myselectshop.entity.*;
import com.sparta.myselectshop.exception.*;
import com.sparta.myselectshop.naver.dto.*;
import com.sparta.myselectshop.repository.*;
import java.util.*;
import lombok.*;
import org.springframework.context.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductFolderRepository productFolderRepository;
    private final FolderRepository folderRepository;
    private final MessageSource messageSource;
    public static final int MIN_MY_PRICE = 100;

    public ProductResponseDto createProduct(ProductRequestDto requestDto, User user) {
        Product product = productRepository.save(new Product(requestDto, user));
        return new ProductResponseDto(product);
    }

    @Transactional
    public ProductResponseDto updateProduct(Long id, ProductMypriceRequestDto requestDto) {
        int myprice = requestDto.getMyprice();
        if (myprice < MIN_MY_PRICE) {
            throw new IllegalArgumentException(
                messageSource.getMessage(
                    "below.min.my.price", new Integer[]{MIN_MY_PRICE}, "Wrong Price",
                    Locale.getDefault()
                ));
        }
        Product product = productRepository.findById(id).orElseThrow(() ->
            new ProductNotFoundException(messageSource.getMessage(
                "not.found.product",
                null,
                "Not Found Product",
                Locale.getDefault()
            ))
        );
        product.update(requestDto);
        return new ProductResponseDto(product);
    }

    @Transactional(readOnly = true)
    public Page<ProductResponseDto> getProducts(User user, int page, int size, String sortBy,
        boolean isAsc) {
        //페이징 처리
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        UserRoleEnum userRoleEnum = user.getRole();

        Page<Product> productList;

        if (userRoleEnum == UserRoleEnum.USER) {
            productList = productRepository.findAllByUser(user, pageable);
        } else {
            productList = productRepository.findAll(pageable);
        }
        return productList.map(ProductResponseDto::new);
    }

    @Transactional
    public void updateBySearch(Long id, ItemDto itemDto) {
        Product product = productRepository.findById(id).orElseThrow(() ->
            new NullPointerException("해당 상품은 지금 없어요!")
        );
        product.updateByItemDto(itemDto);
    }


    public void addFolder(Long productId, Long folderId, User user) {
        Product product = productRepository.findById(productId
        ).orElseThrow(
            () -> new NullPointerException("해당 상품은 없어요!")
        );
        Folder folder = folderRepository.findById(folderId).orElseThrow(
            () -> new NullPointerException("해당 폴더는 없어요!")
        );
        if (!product.getUser().getId().equals(user.getId()) || !folder.getUser().getId()
            .equals(user.getId())) {
            throw new IllegalArgumentException("회원님의 관심상품이 아니거나, 회원님의 폴더가 아니에요!");
        }
        Optional<ProductFolder> overlapFolder = productFolderRepository.findByProductAndFolder(
            product, folder);
        if (overlapFolder.isPresent()) {
            throw new IllegalArgumentException("폴더가 중복이에요!");
        }
        productFolderRepository.save(new ProductFolder(product, folder));

    }

    public Page<ProductResponseDto> getProductsInFolder(Long folderId, int page, int size,
        String sortBy, boolean isAsc, User user) {
        //페이징 처리
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Product> productList = productRepository.findAllByUserAndProductFolderList_FolderId(
            user, folderId, pageable);
        Page<ProductResponseDto> responseDtosList = productList.map(ProductResponseDto::new);
        return responseDtosList;
    }
}
