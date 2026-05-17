package com.psw.management.repository;

import com.psw.management.entity.TypeElement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeElementRepository extends JpaRepository<TypeElement, Long> {
}
