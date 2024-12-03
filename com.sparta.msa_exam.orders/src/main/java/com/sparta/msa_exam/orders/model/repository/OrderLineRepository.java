package com.sparta.msa_exam.orders.model.repository;

import com.sparta.msa_exam.orders.model.entity.OrderLineEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderLineRepository extends JpaRepository<OrderLineEntity, Long> {
    List<OrderLineEntity> findByOrderEntityIdIn(List<Long> orderIds);
}