package com.sparta.msa_exam.orders.domain.service;

import com.sparta.msa_exam.orders.domain.dto.req.ReqOrderPostDTO;
import com.sparta.msa_exam.orders.domain.dto.req.ReqOrderPutDTO;
import com.sparta.msa_exam.orders.domain.dto.res.*;
import com.sparta.msa_exam.orders.domain.external.product.client.ProductClient;
import com.sparta.msa_exam.orders.domain.external.product.dto.res.ResProductGetByIdDTO;
import com.sparta.msa_exam.orders.domain.external.product.dto.res.ResProductGetByIdsDTO;
import com.sparta.msa_exam.orders.model.entity.OrderEntity;
import com.sparta.msa_exam.orders.model.entity.OrderLineEntity;
import com.sparta.msa_exam.orders.model.repository.OrderLineRepository;
import com.sparta.msa_exam.orders.model.repository.OrderRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
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
    @CircuitBreaker(name = "orderService", fallbackMethod = "handleOrderPostFailure")
    public ResponseEntity<ResDTO<ResOrderPostDTO>> postBy(Long userId, String username, ReqOrderPostDTO dto) {

        /*
            FIXME: 현재는 `username`을 직접 파라미터로 받아서 엔티티에 삽입하고 있으나,
                   추후에는 `userId` 를 통해 Auth 도메인(FEIGN Client)을 호출하여
                   유효성을 검증하고, 반환받은 사용자 정보를 사용해 `username`을 삽입하도록 수정할 예정.

            TODO: 동시성 이슈 관련 Lock 설정이 필요하다. (= Redisson 도입)
        */

        ResProductGetByIdsDTO clientBy = productClient.getBy(getIds(dto));

        productClient.reduceBy(dto.getProductList().stream()
                .collect(Collectors.toMap(
                        ReqOrderPostDTO.Product::getProductId,
                        ReqOrderPostDTO.Product::getCount
                )));

        OrderEntity orderEntity = orderRepository.save(dto.toEntityWith(userId, username, getMap(clientBy)));

        return new ResponseEntity<>(
                ResDTO.<ResOrderPostDTO>builder()
                        .code(HttpStatus.OK.value())
                        .message("주문 생성에 성공하였습니다.")
                        .data(ResOrderPostDTO.of(orderEntity))
                        .build(),
                HttpStatus.OK
        );
    }

    public ResponseEntity<ResDTO<Object>> handleOrderPostFailure(Long userId, String username, ReqOrderPostDTO dto, Throwable t) {
        return new ResponseEntity<>(
                ResDTO.builder()
                        .code(HttpStatus.SERVICE_UNAVAILABLE.value())
                        .message("주문 처리에 실패했습니다. 잠시 후 다시 시도해주세요.")
                        .data(ResOrderFailureDTO.of(
                                t.getLocalizedMessage(),
                                userId,
                                username,
                                dto
                        ))
                        .build(),
                HttpStatus.SERVICE_UNAVAILABLE
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

        orderLineRepository.findByProductId(dto.getProduct().getProductId())
                .ifPresentOrElse(
                        orderLineEntity -> {
                            orderLineEntity.updateCount(dto.getProduct().getCount());
                        },
                        () -> {
                            ResProductGetByIdDTO clientBy = productClient.getBy(dto.getProduct().getProductId());
                            orderEntity.addOrderLienEntity(dto.getProduct().toEntityWith(clientBy.getProduct().getSupplyPrice()));
                        }
                );

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

    private static Map<Long, Integer> getMap(ResProductGetByIdsDTO clientBy) {
        return clientBy.getProducts()
                .stream()
                .collect(Collectors.toMap(
                        ResProductGetByIdsDTO.Product::getProductId,
                        ResProductGetByIdsDTO.Product::getSupplyPrice
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
