package com.kakaopay.sprinklerestapi.sprinkling.service.dto;

import com.kakaopay.sprinklerestapi.generic.money.domain.Money;
import com.kakaopay.sprinklerestapi.sprinkling.domain.Sprinkling;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateSprinklingResponseDto {
    private Long id;
    private String roomId;
    private Long creatorId;
    private Integer peopleCount;
    private Long sprinkledMoney;
    private String token;
    private Long maxRandomMoney;

    @Builder
    private CreateSprinklingResponseDto(Long id, String roomId, Long creatorId, Integer peopleCount, Money sprinkledMoney, String token, Money maxRandomMoney){
        this.id = id;
        this.roomId = roomId;
        this.creatorId = creatorId;
        this.peopleCount = peopleCount;
        this.sprinkledMoney = sprinkledMoney.longValue();
        this.token = token;
        this.maxRandomMoney = maxRandomMoney.longValue();
    }

    public static CreateSprinklingResponseDto of(Sprinkling sprinkling){
        return CreateSprinklingResponseDto.builder()
                .id(sprinkling.getId())
                .roomId(sprinkling.getRoomId())
                .creatorId(sprinkling.getCreatorId())
                .peopleCount(sprinkling.getPeopleCount())
                .sprinkledMoney(sprinkling.getSprinkledMoney())
                .token(sprinkling.getToken())
                .maxRandomMoney(sprinkling.getMaxRandomMoney())
                .build();
    }
}
