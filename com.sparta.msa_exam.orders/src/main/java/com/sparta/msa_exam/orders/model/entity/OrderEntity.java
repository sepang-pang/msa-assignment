package com.sparta.msa_exam.orders.model.entity;

import com.sparta.msa_exam.orders.model.constraint.StatusType;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.PERSIST;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "orders")
public class OrderEntity {

    @Id @Tsid
    @Column(name = "order_id")
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "status", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private StatusType statusType;

    @OneToMany(mappedBy = "orderEntity", cascade = PERSIST, orphanRemoval = true)
    private List<OrderLineEntity> orderLineEntities = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "modified_at" , nullable = false)
    private LocalDateTime modifiedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "created_by", updatable = false)
    private String createdBy;

    @Column(name = "modified_by")
    private String modifiedBy;

    @Column(name = "deleted_by")
    private String deletedBy;

    @Builder
    public OrderEntity (Long userId, String username, List<OrderLineEntity> orderLineEntities) {
        this.userId = userId;
        this.createdBy = username;
        this.modifiedBy = username;
        this.statusType = StatusType.PROCESSING; // FIXME : 추후 결제 기능이 구현되면, PENDING 으로 초기화
        addOrderLineEntities(orderLineEntities);
    }

    // == 연관관계 메서드 == //
    public void addOrderLineEntities(List<OrderLineEntity> orderLineEntities) {
        this.orderLineEntities.addAll(orderLineEntities);
        orderLineEntities.forEach(orderLineEntity -> orderLineEntity.updateOrderEntity(this));
    }
}
