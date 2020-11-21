package com.kakaopay.sprinklerestapi.sprinkling.service;

import com.kakaopay.sprinklerestapi.generic.money.domain.Money;
import com.kakaopay.sprinklerestapi.sprinkling.domain.Sprinkling;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SprinklingUpdateResponseDto {
    private Long id;
    private String roomId;
    private Long creatorId;
    private Long receiverId;
    private Money receivedMoney;
    private boolean isMaxAmount;

    public static SprinklingUpdateResponseDto of(Sprinkling sprinkling, Long receiverId, Money receivedMoney){
        return SprinklingUpdateResponseDto.builder()
                .id(sprinkling.getId())
                .roomId(sprinkling.getRoomId())
                .creatorId(sprinkling.getCreatorId())
                .receiverId(receiverId)
                .receivedMoney(receivedMoney)
                .isMaxAmount(receivedMoney.equals(sprinkling.getMaxRandomMoney()))
                .build();
    }
}
