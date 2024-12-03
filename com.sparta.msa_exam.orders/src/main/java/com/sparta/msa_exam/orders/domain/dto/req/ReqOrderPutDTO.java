package com.sparta.msa_exam.orders.domain.dto.req;

import com.sparta.msa_exam.orders.model.entity.OrderLineEntity;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReqOrderPutDTO {

    @Valid
    @NotNull(message = "상품 정봅를 입력해주세요")
    private Product product;

    @Getter
    @NoArgsConstructor
    public static class Product {

        @NotNull(message = "상품 ID를 입력해주세요")
        private Long productId;

        @NotNull(message = "주문 수량을 입력해주세요")
        private int count;

        public OrderLineEntity toEntityWith(int supplyPrice) {
            return OrderLineEntity.builder()
                    .productId(productId)
                    .count(count)
                    .supplyPrice(supplyPrice * count)
                    .build();
        }
    }
}
