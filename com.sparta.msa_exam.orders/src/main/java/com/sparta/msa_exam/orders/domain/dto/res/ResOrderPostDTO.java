package com.sparta.msa_exam.orders.domain.dto.res;

import com.sparta.msa_exam.orders.model.entity.OrderEntity;
import com.sparta.msa_exam.orders.model.entity.OrderLineEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResOrderPostDTO {

    private Order order;

    public static ResOrderPostDTO of(OrderEntity orderEntity) {
        return ResOrderPostDTO.builder()
                .order(Order.from(orderEntity, orderEntity.getOrderLineEntities()))
                .build();
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    private static class Order {

        private String createdBy;
        private String status;
        private List<OrderLine> orderLines;

        private static Order from(OrderEntity orderEntity, List<OrderLineEntity> orderLineEntities) {
            return Order.builder()
                    .createdBy(orderEntity.getCreatedBy())
                    .status(orderEntity.getStatusType().getStatus())
                    .orderLines(OrderLine.from(orderLineEntities))
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
                return OrderLine.builder()
                        .productId(orderLineEntity.getProductId())
                        .count(orderLineEntity.getCount())
                        .supplyPrice(orderLineEntity.getSupplyPrice())
                        .build();
            }
        }

    }
}
