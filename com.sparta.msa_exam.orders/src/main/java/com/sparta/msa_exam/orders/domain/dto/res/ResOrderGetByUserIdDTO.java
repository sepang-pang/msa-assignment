package com.sparta.msa_exam.orders.domain.dto.res;


import com.sparta.msa_exam.orders.model.entity.OrderEntity;
import com.sparta.msa_exam.orders.model.entity.OrderLineEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.PagedModel;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResOrderGetByUserIdDTO {

    private OrderPage orderPage;

    public static ResOrderGetByUserIdDTO of(Page<OrderEntity> orderPage,  Map<Long, List<OrderLineEntity>> orderLinesGroupedByOrderId) {
        return ResOrderGetByUserIdDTO.builder()
                .orderPage(new OrderPage(orderPage, orderLinesGroupedByOrderId))
                .build();
    }

    public static class OrderPage extends PagedModel<OrderPage.Order> {

        private OrderPage(Page<OrderEntity> orderPage, Map<Long, List<OrderLineEntity>> orderLinesGroupedByOrderId) {
            super(
                    new PageImpl<>(
                            Order.from(orderPage.getContent(), orderLinesGroupedByOrderId),
                            orderPage.getPageable(),
                            orderPage.getTotalElements()
                    )
            );
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

            private static List<Order> from(List<OrderEntity> orderEntities,  Map<Long, List<OrderLineEntity>> orderLinesGroupedByOrderId) {
                return orderEntities.stream()
                        .map(orderEntity -> from(orderEntity, orderLinesGroupedByOrderId))
                        .toList();
            }

            private static Order from(OrderEntity orderEntity, Map<Long, List<OrderLineEntity>> orderLinesGroupedByOrderId) {
                return Order.builder()
                        .orderId(orderEntity.getId())
                        .status(orderEntity.getStatusType().getStatus())
                        .createdAt(orderEntity.getCreatedAt())
                        .orderLines(OrderLine.from(orderLinesGroupedByOrderId.get(orderEntity.getId())))
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
}
