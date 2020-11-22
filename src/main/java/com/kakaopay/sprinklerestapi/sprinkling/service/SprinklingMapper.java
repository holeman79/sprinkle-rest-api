package com.kakaopay.sprinklerestapi.sprinkling.service;

import com.kakaopay.sprinklerestapi.generic.money.domain.Money;
import com.kakaopay.sprinklerestapi.sprinkling.domain.RandomMoneySplitter;
import com.kakaopay.sprinklerestapi.sprinkling.domain.Receiving;
import com.kakaopay.sprinklerestapi.sprinkling.domain.Sprinkling;
import com.kakaopay.sprinklerestapi.sprinkling.domain.TokenProvider;
import com.kakaopay.sprinklerestapi.sprinkling.service.dto.CreateSprinklingRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
@RequiredArgsConstructor
public class SprinklingMapper {
    private final RandomMoneySplitter randomMoneySplitter;

    private final TokenProvider tokenProvider;

    public Sprinkling mapFrom(CreateSprinklingRequestDto createRequest, Long creatorId, String roomId){
        Money sprinkledMoney = Money.wons(createRequest.getSprinkledMoney());
        int peopleCount = createRequest.getPeopleCount();
        String token = tokenProvider.generateToken();

        Sprinkling sprinkling = new Sprinkling(
                roomId, creatorId, peopleCount, sprinkledMoney, token,
                makeReceivings(sprinkledMoney, peopleCount)
        );
        return sprinkling;
    }

    private List<Receiving> makeReceivings(Money sprinkledMoney, int peopleCount) {
        return randomMoneySplitter.split(sprinkledMoney, peopleCount)
                .stream()
                .map(Receiving::new)
                .collect(toList());
    }

}
