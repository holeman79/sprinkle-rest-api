package com.kakaopay.sprinklerestapi.sprinkling.service;

import com.kakaopay.sprinklerestapi.generic.money.domain.Money;
import com.kakaopay.sprinklerestapi.sprinkling.domain.RandomMoneySplitter;
import com.kakaopay.sprinklerestapi.sprinkling.domain.Receiving;
import com.kakaopay.sprinklerestapi.sprinkling.domain.Sprinkling;
import com.kakaopay.sprinklerestapi.sprinkling.domain.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
@RequiredArgsConstructor
public class SprinklingMapper {
    private final RandomMoneySplitter randomMoneySplitter;

    private final TokenProvider tokenProvider;

    public Sprinkling mapFrom(SprinklingCreateRequestDto createRequest, Long creatorId, String roomId){
        Money amount = Money.wons(createRequest.getAmount());
        int peopleCount = createRequest.getPeopleCount();
        String token = tokenProvider.generateToken();

        Sprinkling sprinkling = new Sprinkling(
                roomId, creatorId, peopleCount, amount, token,
                makeReceivings(amount, peopleCount)
        );
        return sprinkling;
    }

    private List<Receiving> makeReceivings(Money amount, int peopleCount) {
        return randomMoneySplitter.split(amount, peopleCount)
                .stream()
                .map(Receiving::new)
                .collect(toList());
    }

}
