package com.sparta.myselectshop.repository;

import com.sparta.myselectshop.entity.*;
import java.util.*;
import org.springframework.data.jpa.repository.*;

public interface ProductFolderRepository extends JpaRepository<ProductFolder, Long> {

    Optional<ProductFolder> findByProductAndFolder(Product product, Folder folder);
}
