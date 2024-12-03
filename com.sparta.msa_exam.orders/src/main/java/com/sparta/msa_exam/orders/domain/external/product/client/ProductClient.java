package com.sparta.msa_exam.orders.domain.external.product.client;

import com.sparta.msa_exam.orders.domain.external.product.dto.res.ResProductGetByIdDTO;
import com.sparta.msa_exam.orders.domain.external.product.dto.res.ResProductGetDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "product-service")
public interface ProductClient {

    @PostMapping("/products/details")
    ResProductGetDTO getBy(@RequestBody List<Long> productIds);

    @GetMapping("/products/details/{productId}")
    ResProductGetByIdDTO getBy(@PathVariable(name = "productId") Long productId);
}
