package com.sparta.msa_exam.product.domain.controller;

import com.sparta.msa_exam.product.domain.external.order.dto.res.ResProductForOderDTO;
import com.sparta.msa_exam.product.domain.external.order.dto.res.ResProductsForOrderDTO;
import com.sparta.msa_exam.product.domain.dto.req.ReqProductPostDTO;
import com.sparta.msa_exam.product.domain.dto.res.*;
import com.sparta.msa_exam.product.domain.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j(topic = "ProductController Log")
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    /*
        TODO : Owner 접근 권한 처리 필요
    */
    @PostMapping
    public ResponseEntity<ResDTO<ResProductPostDTO>> postBy(@RequestHeader("X-User-Name") String username,
                                                            @RequestBody ReqProductPostDTO dto) {
        return productService.postBy(username, dto);
    }

    /*
        TODO : 가격 및 이름에 따른 동적 쿼리 구현 고려
    */
    @GetMapping
    public ResponseEntity<ResDTO<ResProductGetDTO>> getBy(
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {

        return productService.getBy(pageable);
    }

    @PostMapping("/details")
    public ResProductsForOrderDTO getBy(@RequestBody List<Long> productIds) {
        return productService.getBy(productIds);
    }

    @GetMapping("/details/{productId}")
    public ResProductForOderDTO getBy(@PathVariable("productId") Long productId) {
        return productService.getBy(productId);
    }

    @PutMapping("/reduce-quantity")
    public void reduceBy(@RequestBody Map<Long, Integer> productIdToQuantityMap) {
        productService.reduceBy(productIdToQuantityMap);
    }

}
