package com.kakaopay.sprinklerestapi.sprinkling.service.dto;

import lombok.*;

import javax.validation.constraints.Positive;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Builder
public class CreateSprinklingRequestDto {
    @Positive(message = "뿌릴 금액을 양수로 입력해주세요.")
    private long sprinkledMoney;

    @Positive(message = "인원을 양수로 입력해주세요.")
    private int peopleCount;
}
