package com.kakaopay.sprinklerestapi.sprinkling.service;

import com.kakaopay.sprinklerestapi.generic.money.domain.Money;
import com.kakaopay.sprinklerestapi.sprinkling.domain.Sprinkling;
import com.kakaopay.sprinklerestapi.sprinkling.domain.SprinklingRepository;
import com.kakaopay.sprinklerestapi.sprinkling.domain.TokenProvider;
import com.kakaopay.sprinklerestapi.sprinkling.domain.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.kakaopay.sprinklerestapi.sprinkling.service.SprinklingDto.*;

@Service
@RequiredArgsConstructor
public class SprinklingService {

    private final Validator validator;
    private final SprinklingRepository sprinklingRepository;
    private final TokenProvider tokenProvider;

    @Transactional
    public Sprinkling createSprinkling(CreateRequest createRequest, int creatorId, String roomId){
        validator.validate(createRequest);
        String token = tokenProvider.generateToken();
        Sprinkling sprinkling = Sprinkling.builder()
                .amount(Money.wons(createRequest.getAmount()))
                .peopleCount(createRequest.getPeopleCount())
                .creatorId(creatorId)
                .roomId(roomId)
                .token(token)
                .build();
        Sprinkling savedSprinkling = sprinklingRepository.save(sprinkling);

        return savedSprinkling;
    }
}
