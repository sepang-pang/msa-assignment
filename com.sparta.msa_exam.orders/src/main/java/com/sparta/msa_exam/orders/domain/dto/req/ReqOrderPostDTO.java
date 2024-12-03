package com.sparta.msa_exam.orders.domain.dto.req;

import com.sparta.msa_exam.orders.model.entity.OrderEntity;
import com.sparta.msa_exam.orders.model.entity.OrderLineEntity;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor
public class ReqOrderPostDTO {

    @Valid
    @NotNull(message = "상품 정보를 입력해주세요")
    private List<Product> productList;

    public OrderEntity toEntityWith(Long userId, String username, Map<Long, Integer> supplyPriceMap) {
        return OrderEntity.builder()
                .userId(userId)
                .username(username)
                .orderLineEntities(
                        productList.stream()
                        .map(product -> product.from(supplyPriceMap.get(product.getProductId())))
                        .toList())
                .build();
    }

    @Getter
    @NoArgsConstructor
    public static class Product {

        @NotNull(message = "상품 ID를 입력해주세요")
        private Long productId;

        @NotNull(message = "주문 수량을 입력해주세요")
        private int count;

        private OrderLineEntity from(int supplyPrice) {
            return OrderLineEntity.builder()
                    .productId(productId)
                    .count(count)
                    .supplyPrice(supplyPrice * count)
                    .build();
        }
    }
}
