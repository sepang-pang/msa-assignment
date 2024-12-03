package com.sparta.msa_exam.orders.domain.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResOrderFailureDTO<T> {

    private FailureDetails<T> failureDetails;

    public static <T> ResOrderFailureDTO<T> of(String error, Long userId, String username, T requestPayload) {
        return ResOrderFailureDTO.<T>builder()
                .failureDetails(FailureDetails.from(error, userId, username, requestPayload))
                .build();
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    private static class FailureDetails<T> {

        private String error;
        private Long userId;
        private String username;
        private T requestPayload;

        private static <T> FailureDetails<T> from(String error, Long userId, String username, T requestPayload) {
            return FailureDetails.<T>builder()
                    .error(error)
                    .userId(userId)
                    .username(username)
                    .requestPayload(requestPayload)
                    .build();
        }
    }
}
