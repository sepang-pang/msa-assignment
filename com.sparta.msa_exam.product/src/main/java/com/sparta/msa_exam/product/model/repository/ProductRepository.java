package com.sparta.msa_exam.product.model.repository;

import com.sparta.msa_exam.product.model.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    Optional<ProductEntity> findByNameAndDeletedAtIsNull(String name);

    List<ProductEntity> findByIdInAndDeletedAtIsNull(List<Long> productIds);

    Page<ProductEntity> findByDeletedAtIsNull(Pageable pageable);

}
