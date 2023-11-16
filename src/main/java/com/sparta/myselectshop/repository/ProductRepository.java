package com.sparta.myselectshop.repository;

import com.sparta.myselectshop.entity.*;
import java.util.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;

public interface ProductRepository extends JpaRepository<Product,Long> {

    Page<Product> findAllByUser(User user, Pageable pageable);
}
