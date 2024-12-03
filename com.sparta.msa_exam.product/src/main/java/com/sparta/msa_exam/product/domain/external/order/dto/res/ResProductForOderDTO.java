package com.sparta.msa_exam.product.domain.external.order.dto.res;

import com.sparta.msa_exam.product.model.entity.ProductEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResProductForOderDTO {

    private Product product;

    public static ResProductForOderDTO of(ProductEntity productEntity) {
        return ResProductForOderDTO.builder()
                .product(Product.from(productEntity))
                .build();
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    private static class Product {
        private Long productId;
        private String name;
        private int supplyPrice;

        private static Product from(ProductEntity productEntity) {
            return Product.builder()
                    .productId(productEntity.getId())
                    .name(productEntity.getName())
                    .supplyPrice(productEntity.getSupplyPrice())
                    .build();
        }
    }
}
