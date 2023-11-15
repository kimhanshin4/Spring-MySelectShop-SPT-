package com.sparta.myselectshop.repository;

import com.sparta.myselectshop.entity.*;
import java.util.*;
import org.springframework.data.jpa.repository.*;

public interface ProductRepository extends JpaRepository<Product,Long> {

    List<Product> findAllByUser(User user);
}
