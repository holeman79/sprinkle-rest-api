package com.kakaopay.sprinklerestapi.sprinkling.service;


import com.kakaopay.sprinklerestapi.generic.money.domain.Money;
import com.kakaopay.sprinklerestapi.sprinkling.domain.RandomMoneySplitter;
import com.kakaopay.sprinklerestapi.sprinkling.domain.Sprinkling;
import com.kakaopay.sprinklerestapi.sprinkling.domain.TokenProvider;
import com.kakaopay.sprinklerestapi.sprinkling.service.dto.CreateSprinklingRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class SprinklingMapperTest {
    @InjectMocks
    private SprinklingMapper sprinklingMapper;

    @Mock
    private TokenProvider tokenProvider;

    @Mock
    private RandomMoneySplitter randomMoneySplitter;

    @Test
    @DisplayName("뿌리기 mapping 테스트")
    public void sprinkling_mapping_test(){
        // given
        long requestMoney = 10000L;
        int peopleCount = 4;
        CreateSprinklingRequestDto createSprinklingRequestDto = CreateSprinklingRequestDto.builder()
                .sprinkledMoney(requestMoney)
                .peopleCount(peopleCount)
                .build();

        Long creatorId = 1L;
        String roomId = "R1";
        String token = "a0R";
        Money sprinkledMoney = Money.wons(requestMoney);
        List<Money> splattedMoneys = Arrays.asList(Money.wons(5000), Money.wons(2500),
                Money.wons(1500), Money.wons(1000));

        given(tokenProvider.generateToken()).willReturn(token);
        given(randomMoneySplitter.split(sprinkledMoney, peopleCount))
                .willReturn(splattedMoneys);

        // when
        Sprinkling sprinkling = sprinklingMapper.mapFrom(createSprinklingRequestDto, creatorId, roomId);

        // then
        assertThat(sprinkling.getCreatorId()).isEqualTo(creatorId);
        assertThat(sprinkling.getRoomId()).isEqualTo(roomId);
        assertThat(sprinkling.getSprinkledMoney()).isEqualTo(sprinkledMoney);
        assertThat(sprinkling.getPeopleCount()).isEqualTo(peopleCount);
        assertThat(sprinkling.getToken()).isEqualTo(token);
        assertThat(sprinkling.getReceivings().size()).isEqualTo(splattedMoneys.size());
    }
}
