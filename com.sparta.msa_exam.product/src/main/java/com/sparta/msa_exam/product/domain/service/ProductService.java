package com.sparta.msa_exam.product.domain.service;

import com.sparta.msa_exam.product.domain.dto.req.ReqProductPostDTO;
import com.sparta.msa_exam.product.domain.dto.res.ResDTO;
import com.sparta.msa_exam.product.domain.dto.res.ResProductGetDTO;
import com.sparta.msa_exam.product.domain.dto.res.ResProductPostDTO;
import com.sparta.msa_exam.product.domain.external.order.dto.res.ResProductForOderDTO;
import com.sparta.msa_exam.product.domain.external.order.dto.res.ResProductsForOrderDTO;
import com.sparta.msa_exam.product.model.entity.ProductEntity;
import com.sparta.msa_exam.product.model.repository.ProductRepository;
import com.sun.jdi.request.DuplicateRequestException;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "Product Service Log")
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional
    public ResponseEntity<ResDTO<ResProductPostDTO>> postBy(String username, ReqProductPostDTO dto) {

        /*
            중복 이름 검증
            - 예외 커스텀 고려
        */
        productRepository.findByNameAndDeletedAtIsNull(dto.getProduct().getName())
                .ifPresent(productEntity -> {
                    throw new DuplicateRequestException("이미 존재하는 상품입니다.");
                });


       /*
            상품 생성
       */
        ProductEntity productEntity = productRepository.save(dto.getProduct().toEntityWith(username));

        return new ResponseEntity<>(
                ResDTO.<ResProductPostDTO>builder()
                        .code(HttpStatus.OK.value())
                        .message("상품 생성에 성공하였습니다.")
                        .data(ResProductPostDTO.of(productEntity))
                        .build(),
                HttpStatus.OK
        );
    }

    @Transactional(readOnly = true)
    public ResponseEntity<ResDTO<ResProductGetDTO>> getBy(Pageable pageable) {

        Page<ProductEntity> productEntityPage = productRepository.findByDeletedAtIsNull(pageable);

        return new ResponseEntity<>(
                ResDTO.<ResProductGetDTO>builder()
                        .code(HttpStatus.OK.value())
                        .message("상품 조회에 성공했습니다.")
                        .data(ResProductGetDTO.of(productEntityPage))
                        .build(),
                HttpStatus.OK
        );
    }

    @Transactional(readOnly = true)
    public ResProductsForOrderDTO getBy(List<Long> productIds) {
        List<ProductEntity> productEntities = productRepository.findByIdInAndDeletedAtIsNull(productIds);

        /*
             XXX:
              1. 재고 체크를 Product 에서 수행할 것인가, Order 에서 수행할 것인가에 대한 고민 중
              2. 재고 부족 발생 시 "OutOfStockException" 라는 커스텀 예외 클래스를 만드는 것을 고려해볼만 함 (현재는 임시로 예외 설정하였음)
        */

        for (ProductEntity productEntity : productEntities) {
            if (productEntity.getQuantity() <= 0) {
                throw new IllegalArgumentException("상품 재고가 부족합니다.");
            }
        }

        return ResProductsForOrderDTO.of(productEntities);
    }

    @Transactional(readOnly = true)
    public ResProductForOderDTO getBy(Long productId) {
        return ResProductForOderDTO.of(productRepository.findByIdAndDeletedAtIsNull(productId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 항목입니다.")));
    }

    @Transactional
    public void reduceBy(Map<Long, Integer> productIdToQuantityMap) {
        List<ProductEntity> productEntities = productRepository
                .findByIdInAndDeletedAtIsNull(new ArrayList<>(productIdToQuantityMap.keySet()));

        productEntities.forEach(productEntity ->
                productEntity.reduceQuantity(productIdToQuantityMap.get(productEntity.getId()))
        );
    }

}
