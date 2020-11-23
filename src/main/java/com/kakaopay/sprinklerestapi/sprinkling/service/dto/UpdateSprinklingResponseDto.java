package com.kakaopay.sprinklerestapi.sprinkling.service.dto;

import com.kakaopay.sprinklerestapi.generic.money.domain.Money;
import com.kakaopay.sprinklerestapi.sprinkling.domain.Sprinkling;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UpdateSprinklingResponseDto {
    private Long id;
    private String roomId;
    private Long creatorId;
    private Long receiverId;
    private Long receivedMoney;
    private boolean isMaxRandomMoney;

    public boolean getIsMaxRandomMoney(){
        return isMaxRandomMoney;
    }

    public static UpdateSprinklingResponseDto of(Sprinkling sprinkling, Long receiverId, Money receivedMoney){
        return UpdateSprinklingResponseDto.builder()
                .id(sprinkling.getId())
                .roomId(sprinkling.getRoomId())
                .creatorId(sprinkling.getCreatorId())
                .receiverId(receiverId)
                .receivedMoney(receivedMoney.longValue())
                .isMaxRandomMoney(receivedMoney.equals(sprinkling.getMaxRandomMoney()))
                .build();
    }
}
