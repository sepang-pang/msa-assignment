package com.sparta.msa_exam.product.domain.service;

import com.sparta.msa_exam.product.domain.dto.req.ReqProductPostDTO;
import com.sparta.msa_exam.product.domain.dto.res.ResDTO;
import com.sparta.msa_exam.product.domain.dto.res.ResProductGetDTO;
import com.sparta.msa_exam.product.domain.dto.res.ResProductPostDTO;
import com.sparta.msa_exam.product.model.entity.ProductEntity;
import com.sparta.msa_exam.product.model.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "Product Service Log")
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional
    public ResponseEntity<ResDTO<ResProductPostDTO>> postBy(String username, ReqProductPostDTO dto) {

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

}
