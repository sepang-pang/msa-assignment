package com.sparta.msa_exam.product.domain.dto.res;

import com.sparta.msa_exam.product.model.entity.ProductEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.PagedModel;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResProductGetDTO {

    private ProductPage productPage;

    public static ResProductGetDTO of(Page<ProductEntity> productEntityPage) {
        return ResProductGetDTO.builder()
                .productPage(new ProductPage(productEntityPage))
                .build();
    }

    @Getter
    private static class ProductPage extends PagedModel<ProductPage.Product> {
        public ProductPage(Page<ProductEntity> productPage) {
            super(
                    new PageImpl<>(
                            Product.from(productPage.getContent()),
                            productPage.getPageable(),
                            productPage.getTotalElements()
                    )
            );
        }

        @Getter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        private static class Product {

            private Long id;
            private String name;
            private int supplyPrice;
            private int quantity;

            private static List<Product> from(List<ProductEntity> productEntities) {
                return productEntities.stream()
                        .map(Product::from)
                        .toList();
            }

            private static Product from(ProductEntity productEntity) {
                return Product.builder()
                        .id(productEntity.getId())
                        .name(productEntity.getName())
                        .supplyPrice(productEntity.getSupplyPrice())
                        .quantity(productEntity.getQuantity())
                        .build();
            }
        }
    }
}
