package com.sparta.msa_exam.orders.domain.service;

import com.sparta.msa_exam.orders.domain.dto.req.ReqOrderPostDTO;
import com.sparta.msa_exam.orders.domain.dto.req.ReqOrderPutDTO;
import com.sparta.msa_exam.orders.domain.dto.res.*;
import com.sparta.msa_exam.orders.domain.external.ProductClient;
import com.sparta.msa_exam.orders.model.entity.OrderEntity;
import com.sparta.msa_exam.orders.model.entity.OrderLineEntity;
import com.sparta.msa_exam.orders.model.repository.OrderLineRepository;
import com.sparta.msa_exam.orders.model.repository.OrderRepository;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final ProductClient productClient;
    private final OrderRepository orderRepository;
    private final OrderLineRepository orderLineRepository;


    @Transactional
    public ResponseEntity<ResDTO<ResOrderPostDTO>> postBy(Long userId, String username, ReqOrderPostDTO dto) {

        /*
            TODO: 상품 재고 검증 하기
            FIXME: 현재는 `username`을 직접 파라미터로 받아서 엔티티에 삽입하고 있으나,
                   추후에는 `userId` 를 통해 Auth 도메인(FEIGN Client)을 호출하여
                   유효성을 검증하고, 반환받은 사용자 정보를 사용해 `username`을 삽입하도록 수정할 예정.
        */

        ResProductGetDTO clientBy = productClient.getBy(getIds(dto));

        Map<Long, Integer> supplyPriceMap = getMap(clientBy);

        OrderEntity orderEntity = orderRepository.save(dto.toEntityWith(userId, username, supplyPriceMap));

        return new ResponseEntity<>(
                ResDTO.<ResOrderPostDTO>builder()
                        .code(HttpStatus.OK.value())
                        .message("주문 생성에 성공하였습니다.")
                        .data(ResOrderPostDTO.of(orderEntity))
                        .build(),
                HttpStatus.OK
        );
    }

    @Transactional(readOnly = true)
    public ResponseEntity<ResDTO<ResOrderGetByUserIdDTO>> getBy(Long userId, Pageable pageable) {

        Page<OrderEntity> orderEntityPage = orderRepository.findByUserIdAndDeletedAtIsNull(userId, pageable);

        return new ResponseEntity<>(
                ResDTO.<ResOrderGetByUserIdDTO>builder()
                        .code(HttpStatus.OK.value())
                        .message("주문 조회에 성공하였습니다.")
                        .data(ResOrderGetByUserIdDTO.of(orderEntityPage, getMap(orderEntityPage)))
                        .build(),
                HttpStatus.OK
        );
    }

    @Transactional(readOnly = true)
    public ResponseEntity<ResDTO<ResOrderGetByIdDTO>> getBy(Long oderId) {

        OrderEntity orderEntity = getOrderEntity(oderId);

        return new ResponseEntity<>(
                ResDTO.<ResOrderGetByIdDTO>builder()
                        .code(HttpStatus.OK.value())
                        .message("주문 조회에 성공했습니다.")
                        .data(ResOrderGetByIdDTO.of(orderEntity))
                        .build(),
                HttpStatus.OK
        );
    }

    @Transactional
    public ResponseEntity<ResDTO<Object>> putBy(Long userId, Long orderId, ReqOrderPutDTO dto) {

        OrderEntity orderEntity = getOrderEntity(orderId);

        if (!orderEntity.getUserId().equals(userId)) {
            throw new IllegalArgumentException("접근 권한이 없습니다.");
        }

        /*
            FIXME: 동일한 Product 가 존재하면, feign X
        */

        ResProductGetByIdDTO clientBy = productClient.getBy(dto.getProduct().getProductId());

        orderEntity.addOrderLienEntity(dto.getProduct().toEntityWith(clientBy.getProduct().getSupplyPrice()));

        return new ResponseEntity<>(
                ResDTO.builder()
                        .code(HttpStatus.OK.value())
                        .message("주문 추가에 성공하였습니다.")
                        .build(),
                HttpStatus.OK
        );
    }

    private OrderEntity getOrderEntity(Long oderId) {
        return orderRepository.findByIdAndDeletedAtIsNull(oderId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 항목입니다."));
    }

    private static Map<Long, Integer> getMap(ResProductGetDTO clientBy) {
        return clientBy.getProducts()
                .stream()
                .collect(Collectors.toMap(
                        ResProductGetDTO.Product::getProductId,
                        ResProductGetDTO.Product::getSupplyPrice
                ));
    }

    private Map<Long, List<OrderLineEntity>> getMap(Page<OrderEntity> orderEntityPage) {
        List<Long> ids = getIds(orderEntityPage);

        List<OrderLineEntity> orderLineEntities = orderLineRepository.findByOrderEntityIdIn(ids);

        return orderLineEntities.stream()
                .collect(Collectors.groupingBy(orderLineEntity -> orderLineEntity.getOrderEntity().getId()));
    }

    private static List<Long> getIds(ReqOrderPostDTO dto) {
        return dto.getProductList().stream()
                .map(ReqOrderPostDTO.Product::getProductId)
                .toList();
    }

    private static List<Long> getIds(Page<OrderEntity> orderEntityPage) {
        return orderEntityPage.getContent().stream()
                .map(OrderEntity::getId)
                .toList();
    }
}
