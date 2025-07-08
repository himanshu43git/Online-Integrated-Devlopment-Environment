package com.oide.profile.repositories;

import com.oide.profile.entity.UserFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FileRepository extends JpaRepository<UserFile, Long> {
    @Query("""
      SELECT uf.fileId
        FROM UserFile uf
       WHERE uf.profile.userId = :userId
      """)
    Optional<List<String>> findFileIdsByUserId(@Param("userId") Long userId);
}
