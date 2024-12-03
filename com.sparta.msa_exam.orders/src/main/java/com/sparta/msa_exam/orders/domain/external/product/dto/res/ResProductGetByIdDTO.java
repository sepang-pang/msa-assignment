package com.sparta.msa_exam.orders.domain.external.product.dto.res;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ResProductGetByIdDTO {

    private Product product;

    @Getter
    @NoArgsConstructor
    public static class Product {
        private Long productId;
        private String name;
        private int supplyPrice;
    }
}
