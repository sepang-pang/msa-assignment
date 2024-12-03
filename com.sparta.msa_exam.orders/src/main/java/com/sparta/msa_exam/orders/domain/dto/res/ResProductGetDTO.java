package com.sparta.msa_exam.orders.domain.dto.res;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ResProductGetDTO {

    private List<Product> products;

    @Getter
    @NoArgsConstructor
    public static class Product {
        private Long productId;
        private String name;
        private int supplyPrice;
    }
}
