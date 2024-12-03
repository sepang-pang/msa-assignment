package com.sparta.msa_exam.orders.domain.external.product.client;

import com.sparta.msa_exam.orders.domain.external.product.dto.res.ResProductGetByIdDTO;
import com.sparta.msa_exam.orders.domain.external.product.dto.res.ResProductGetByIdsDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(name = "product-service")
public interface ProductClient {

    /*
        XXX: 데이터 전달 간 DTO 를 이용 vs 자료형 이용
             현재는 간단히 전달하는 것이기에 기본 자료형을 써도 괜찮지만,
             이런 경우에라도 장기적으로 확장 가능성을 본다면 DTO 로 전달하는 것이 더 좋을 거 같다.
    */

    @PostMapping("/products/details")
    ResProductGetByIdsDTO getBy(@RequestBody List<Long> productIds);

    @GetMapping("/products/details/{productId}")
    ResProductGetByIdDTO getBy(@PathVariable(name = "productId") Long productId);

    @PutMapping("/products/reduce-quantity")
    void reduceBy(@RequestBody Map<Long, Integer> productIdToQuantityMap);
}
