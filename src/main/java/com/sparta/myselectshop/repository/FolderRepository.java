package com.sparta.myselectshop.repository;

import com.sparta.myselectshop.entity.*;
import java.util.*;
import org.springframework.data.jpa.repository.*;

public interface FolderRepository extends JpaRepository<Folder, Long> {

    List<Folder> findAllByUserAndNameIn(User user, List<String> folderNames);
    // SELECT * FROM Folder WHERE user_id = ? and name in (? , ? , ?);
    List<Folder> findAllByUser(User user);
}
