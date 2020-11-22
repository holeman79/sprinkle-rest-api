package com.kakaopay.sprinklerestapi.sprinkling.service;

import com.kakaopay.sprinklerestapi.generic.money.domain.Money;
import com.kakaopay.sprinklerestapi.sprinkling.domain.Sprinkling;
import com.kakaopay.sprinklerestapi.sprinkling.domain.SprinklingRepository;
import com.kakaopay.sprinklerestapi.sprinkling.exception.NotFoundException;
import com.kakaopay.sprinklerestapi.sprinkling.service.dto.CreateSprinklingRequestDto;
import com.kakaopay.sprinklerestapi.sprinkling.service.dto.CreateSprinklingResponseDto;
import com.kakaopay.sprinklerestapi.sprinkling.service.dto.GetSprinklingResponseDto;
import com.kakaopay.sprinklerestapi.sprinkling.service.dto.UpdateSprinklingResponseDto;
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
    public CreateSprinklingResponseDto create(CreateSprinklingRequestDto createRequest, Long creatorId, String roomId){
        sprinklingValidator.validateCreateRequest(createRequest);

        Sprinkling sprinkling = sprinklingMapper.mapFrom(createRequest, creatorId, roomId);
        Sprinkling savedSprinkling = sprinklingRepository.save(sprinkling);

        return CreateSprinklingResponseDto.of(savedSprinkling);
    }

    @Transactional
    public UpdateSprinklingResponseDto receive(Long id, Long receiverId, String roomId, String token){
        Sprinkling sprinkling = getSprinkling(id);
        sprinklingValidator.validateReceiving(sprinkling, receiverId, roomId, token);
        Money receivedMoney = sprinkling.receive(receiverId);

        return UpdateSprinklingResponseDto.of(sprinkling, receiverId, receivedMoney);
    }

    @Transactional(readOnly = true)
    public GetSprinklingResponseDto findById(Long id, Long viewerId, String token){
        Sprinkling sprinkling = getSprinkling(id);
        sprinklingValidator.validateGetSprinkling(sprinkling, viewerId, token);
        return GetSprinklingResponseDto.of(sprinkling);
    }

    private Sprinkling getSprinkling(Long id) {
        return sprinklingRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }
}
