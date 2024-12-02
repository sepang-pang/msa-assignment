package com.sparta.msa_exam.product.model.repository;

import com.sparta.msa_exam.product.model.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
}
