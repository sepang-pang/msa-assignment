package com.sparta.msa_exam.orders.domain.controller;

import com.sparta.msa_exam.orders.domain.dto.req.ReqOrderPostDTO;
import com.sparta.msa_exam.orders.domain.dto.res.ResDTO;
import com.sparta.msa_exam.orders.domain.dto.res.ResOrderPostDTO;
import com.sparta.msa_exam.orders.domain.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<ResDTO<ResOrderPostDTO>> postBy(@RequestHeader("X-User-Id") Long userId,
                                                          @RequestHeader("X-User-Name") String username,
                                                          @RequestBody ReqOrderPostDTO dto) {

        return orderService.postBy(userId, username, dto);
    }
}