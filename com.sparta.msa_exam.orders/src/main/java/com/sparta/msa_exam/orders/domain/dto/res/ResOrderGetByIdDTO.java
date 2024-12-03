package com.sparta.msa_exam.orders.domain.dto.res;

import com.sparta.msa_exam.orders.model.entity.OrderEntity;
import com.sparta.msa_exam.orders.model.entity.OrderLineEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResOrderGetByIdDTO {

    private Order order;

    public static ResOrderGetByIdDTO of(OrderEntity orderEntity) {
        return ResOrderGetByIdDTO.builder()
                .order(Order.from(orderEntity))
                .build();
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    private static class Order {

        private Long orderId;
        private String status;
        private LocalDateTime createdAt;
        private List<OrderLine> orderLines;

        private static Order from(OrderEntity orderEntity) {
            return Order.builder()
                    .orderId(orderEntity.getId())
                    .status(orderEntity.getStatusType().getStatus())
                    .createdAt(orderEntity.getCreatedAt())
                    .orderLines(OrderLine.from(orderEntity.getOrderLineEntities()))
                    .build();
        }

        @Getter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        private static class OrderLine {

            private Long productId;
            private int count;
            private int supplyPrice;

            private static List<OrderLine> from(List<OrderLineEntity> orderLineEntities) {
                return orderLineEntities.stream()
                        .map(OrderLine::from)
                        .toList();
            }

            private static OrderLine from(OrderLineEntity orderLineEntity) {
                return Order.OrderLine.builder()
                        .productId(orderLineEntity.getProductId())
                        .count(orderLineEntity.getCount())
                        .supplyPrice(orderLineEntity.getSupplyPrice())
                        .build();
            }
        }
    }
}
