package com.kakaopay.sprinklerestapi.sprinkling.service;

import com.kakaopay.sprinklerestapi.generic.money.domain.Money;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SprinklingQueryResponseDto {
    private Long id;
    private String roomId;
    private Long creatorId;
    private Long receiverId;
    private Money amount;
    private boolean isMaxAmount;


}
