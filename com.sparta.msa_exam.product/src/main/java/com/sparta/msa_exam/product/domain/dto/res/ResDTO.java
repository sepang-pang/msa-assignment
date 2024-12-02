package com.sparta.msa_exam.product.domain.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResDTO<T> {
    private Integer code;
    private String message;
    private T data;
}