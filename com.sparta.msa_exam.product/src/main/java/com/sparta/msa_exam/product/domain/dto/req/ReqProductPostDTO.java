package com.sparta.msa_exam.product.domain.dto.req;

import com.sparta.msa_exam.product.model.entity.ProductEntity;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReqProductPostDTO {

    @Valid
    @NotNull(message = "상품 정보를 입력해주세요")
    private Product product;

    @Getter
    @NoArgsConstructor
    public static class Product {

        @NotBlank(message = "상품 이름을 입력해주세요")
        private String name;

        @NotNull(message = "상품 가격을 입력해주세요")
        private int supplyPrice;

        @NotNull(message = "상품 재고를 입력해주세요")
        private int quantity;

        public ProductEntity toEntityWith(String username) {
            return ProductEntity.builder()
                    .name(name)
                    .supplyPrice(supplyPrice)
                    .quantity(quantity)
                    .username(username)
                    .build();
        }
    }
}
