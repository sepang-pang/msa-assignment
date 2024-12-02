package com.sparta.msa_exam.orders.model.constraint;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatusType {
    PENDING(Status.PENDING), // 결제 대기중
    PROCESSING(Status.PROCESSING), // 처리중 : 결제가 완료됐고, 재고수량이 줄어든 상태 AND 배송중
    COMPLETED(Status.COMPLETED), // 주문 및 배송 완료
    CANCELED(Status.CANCELED), // 관리자 혹은 주문자에 의한 취소
    REFUNDED(Status.REFUNDED); // 관리자에 의한 환불

    private final String status;

    public static class Status {
        public static final String PENDING = "ORDER_PENDING";
        public static final String PROCESSING = "ORDER_PROCESSING";
        public static final String COMPLETED = "ORDER_COMPLETED";
        public static final String CANCELED = "ORDER_CANCELED";
        public static final String REFUNDED = "ORDER_REFUNDED";
    }
}
