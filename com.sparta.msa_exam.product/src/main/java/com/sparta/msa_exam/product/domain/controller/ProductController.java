package com.sparta.msa_exam.product.domain.controller;

import com.sparta.msa_exam.product.domain.dto.req.ReqProductPostDTO;
import com.sparta.msa_exam.product.domain.dto.res.ResDTO;
import com.sparta.msa_exam.product.domain.dto.res.ResProductGetDTO;
import com.sparta.msa_exam.product.domain.dto.res.ResProductPostDTO;
import com.sparta.msa_exam.product.domain.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j(topic = "ProductController Log")
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    /*
        상품 생성
        - Owner 접근 권한 처리 예정
    */
    @PostMapping
    public ResponseEntity<ResDTO<ResProductPostDTO>> postBy(@RequestHeader("X-User-Name") String username,
                                                            @RequestBody ReqProductPostDTO dto) {
        return productService.postBy(username, dto);
    }

    /*
        상품 전체 조회
        - 로그인 유무에 따른 반환 처리 고려
        - 가격 및 이름에 따른 동적 쿼리 구현 고려
    */
    @GetMapping
    public ResponseEntity<ResDTO<ResProductGetDTO>> getBy(
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {

        return productService.getBy(pageable);
    }

}
