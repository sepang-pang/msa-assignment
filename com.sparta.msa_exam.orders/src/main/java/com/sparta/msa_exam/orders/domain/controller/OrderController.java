package com.sparta.msa_exam.orders.domain.controller;

import com.sparta.msa_exam.orders.domain.dto.req.ReqOrderPostDTO;
import com.sparta.msa_exam.orders.domain.dto.req.ReqOrderPutDTO;
import com.sparta.msa_exam.orders.domain.dto.res.ResDTO;
import com.sparta.msa_exam.orders.domain.dto.res.ResOrderGetByIdDTO;
import com.sparta.msa_exam.orders.domain.dto.res.ResOrderGetByUserIdDTO;
import com.sparta.msa_exam.orders.domain.dto.res.ResOrderPostDTO;
import com.sparta.msa_exam.orders.domain.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    /*
        TODO: USER 만 주문 가능
              USER 접근 권한 추가 필요
    */
    @PostMapping
    public ResponseEntity<ResDTO<ResOrderPostDTO>> postBy(@RequestHeader("X-User-Id") Long userId,
                                                          @RequestHeader("X-User-Name") String username,
                                                          @RequestBody ReqOrderPostDTO dto) {

        return orderService.postBy(userId, username, dto);
    }

    @GetMapping
    public ResponseEntity<ResDTO<ResOrderGetByUserIdDTO>> getBy(@RequestHeader("X-User-Id") Long userId,
                                                                @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {

        return orderService.getBy(userId, pageable);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<ResDTO<ResOrderGetByIdDTO>> getBy(@PathVariable("orderId") Long oderId) {

        return orderService.getBy(oderId);
    }

    /*
        TODO: 수정은 ADMIN 과 본인만 가능
              ADMIN 접근 권한 추가 필요
    */
    @PutMapping("/{orderId}")
    public ResponseEntity<ResDTO<Object>> putBy(@RequestHeader("X-User-Id") Long userId,
                                                @PathVariable("orderId") Long oderId,
                                                @RequestBody ReqOrderPutDTO dto) {

        return orderService.putBy(userId, oderId, dto);
    }

}
