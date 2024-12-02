package com.sparta.msa_exam.orders.model.entity;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "order_lines")
public class OrderLineEntity {

    @Id @Tsid
    @Column(name = "order_line_id")
    private Long id;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "count", nullable = false)
    private int count;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "order_id")
    private OrderEntity orderEntity;

    // == 연관관계 메서드 == //
    public void updateOrderEntity(OrderEntity orderEntity) {
        this.orderEntity = orderEntity;
    }
}
