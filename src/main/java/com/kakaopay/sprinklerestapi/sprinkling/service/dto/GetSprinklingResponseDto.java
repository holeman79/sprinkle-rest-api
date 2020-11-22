package com.kakaopay.sprinklerestapi.sprinkling.service.dto;

import com.kakaopay.sprinklerestapi.sprinkling.domain.Receiving;
import com.kakaopay.sprinklerestapi.sprinkling.domain.Sprinkling;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GetSprinklingResponseDto {
    private Long id;
    private String roomId;
    private Long creatorId;
    private LocalDateTime sprinkledTime;
    private Long sprinkledMoney;
    private Long receivedMoney;
    private Long maxRandomMoney;

    private List<ReceivingDto> receivingDtos;


    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class ReceivingDto{
        private Long receivedMoney;
        private Long receiverId;

        public ReceivingDto(Receiving receiving){
            this.receivedMoney = receiving.getReceivedMoney().longValue();
            this.receiverId = receiving.getReceiverId();
        }
    }

    public static GetSprinklingResponseDto of(Sprinkling sprinkling){
        return GetSprinklingResponseDto.builder()
                .id(sprinkling.getId())
                .roomId(sprinkling.getRoomId())
                .creatorId(sprinkling.getCreatorId())
                .sprinkledTime(sprinkling.getSprinkledTime())
                .sprinkledMoney(sprinkling.getSprinkledMoney().longValue())
                .receivedMoney(sprinkling.getTotalReceivedMoney().longValue())
                .maxRandomMoney(sprinkling.getMaxRandomMoney().longValue())
                .receivingDtos(sprinkling.getReceivingCompletedList().stream()
                                .map(ReceivingDto::new)
                                .collect(Collectors.toList()))
                .build();
    }

}
