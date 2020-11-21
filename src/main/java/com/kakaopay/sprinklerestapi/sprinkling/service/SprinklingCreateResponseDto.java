package com.kakaopay.sprinklerestapi.sprinkling.service;

import com.kakaopay.sprinklerestapi.generic.money.domain.Money;
import com.kakaopay.sprinklerestapi.sprinkling.domain.Sprinkling;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SprinklingCreateResponseDto {
    private Long id;
    private String roomId;
    private Long creatorId;
    private Integer peopleCount;
    private Money amount;
    private String token;
    private Money maxRandomMoney;

    @Builder
    private SprinklingCreateResponseDto(Long id, String roomId, Long creatorId, Integer peopleCount, Money amount, String token, Money maxRandomMoney){
        this.id = id;
        this.roomId = roomId;
        this.creatorId = creatorId;
        this.peopleCount = peopleCount;
        this.amount = amount;
        this.token = token;
        this.maxRandomMoney = maxRandomMoney;
    }

    public static SprinklingCreateResponseDto of(Sprinkling sprinkling){
        return SprinklingCreateResponseDto.builder()
                .id(sprinkling.getId())
                .roomId(sprinkling.getRoomId())
                .creatorId(sprinkling.getCreatorId())
                .peopleCount(sprinkling.getPeopleCount())
                .amount(sprinkling.getAmount())
                .token(sprinkling.getToken())
                .maxRandomMoney(sprinkling.getMaxRandomMoney())
                .build();
    }
}
