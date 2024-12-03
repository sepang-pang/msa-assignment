package com.sparta.msa_exam.orders.model.repository;

import com.sparta.msa_exam.orders.model.entity.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    Page<OrderEntity> findByUserIdAndDeletedAtIsNull(Long userId, Pageable pageable);

    Optional<OrderEntity> findByIdAndDeletedAtIsNull(Long orderId);

}
