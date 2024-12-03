package com.sparta.msa_exam.product.domain.dto.res;

import com.sparta.msa_exam.product.model.entity.ProductEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResProductForOrderDTO {

    private List<Product> products;

    public static ResProductForOrderDTO of(List<ProductEntity> productEntities) {
        return ResProductForOrderDTO.builder()
                .products(Product.from(productEntities))
                .build();
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Product {

        private Long productId;
        private String name;
        private int supplyPrice;

        private static List<Product> from(List<ProductEntity> productEntities) {
            return productEntities.stream()
                    .map(Product::from)
                    .toList();
        }

        private static Product from(ProductEntity productEntity) {
            return Product.builder()
                    .productId(productEntity.getId())
                    .name(productEntity.getName())
                    .supplyPrice(productEntity.getSupplyPrice())
                    .build();
        }
    }
}
