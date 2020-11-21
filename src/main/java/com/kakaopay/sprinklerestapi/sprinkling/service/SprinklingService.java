package com.kakaopay.sprinklerestapi.sprinkling.service;

import com.kakaopay.sprinklerestapi.generic.money.domain.Money;
import com.kakaopay.sprinklerestapi.sprinkling.domain.Sprinkling;
import com.kakaopay.sprinklerestapi.sprinkling.domain.SprinklingRepository;
import com.kakaopay.sprinklerestapi.sprinkling.exception.SprinklingNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SprinklingService {
    private final SprinklingValidator sprinklingValidator;
    private final SprinklingRepository sprinklingRepository;
    private final SprinklingMapper sprinklingMapper;

    @Transactional
    public SprinklingCreateResponseDto create(SprinklingCreateRequestDto createRequest, Long creatorId, String roomId){
        sprinklingValidator.validateCreateRequest(createRequest);

        Sprinkling sprinkling = sprinklingMapper.mapFrom(createRequest, creatorId, roomId);
        Sprinkling savedSprinkling = sprinklingRepository.save(sprinkling);

        return SprinklingCreateResponseDto.of(savedSprinkling);
    }

    @Transactional
    public SprinklingUpdateResponseDto receive(Long id, Long receiverId, String roomId, String token){
        Sprinkling sprinkling = sprinklingRepository.findById(id).orElseThrow(() -> new SprinklingNotFoundException(id));
        sprinklingValidator.validateReceiving(sprinkling, receiverId, roomId, token);
        Money receivedMoney = sprinkling.receiving(receiverId);

        return SprinklingUpdateResponseDto.of(sprinkling, receiverId, receivedMoney);
    }

    @Transactional
    public SprinklingQueryResponseDto findById(Long id, Long creatorId, String token){
        return null;
    }


}
