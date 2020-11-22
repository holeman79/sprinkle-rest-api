package com.kakaopay.sprinklerestapi.sprinkling.service.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Positive;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class CreateSprinklingRequestDto {
    @Positive(message = "뿌릴 금액을 양수로 입력해주세요.")
    private long sprinkledMoney;

    @Positive(message = "인원을 양수로 입력해주세요.")
    private int peopleCount;
}
