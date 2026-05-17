package com.psw.management.repository;

import com.psw.management.entity.Password;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PasswordRepository extends JpaRepository<Password,Long> {
    List<Password> findByUserId(Long userId);

    @Query("SELECT COUNT(p) FROM Password p WHERE p.user.id = :userId AND p.isFavorite = true")
    long countFavoriteByUserId(@Param("userId") Long userId);

}
