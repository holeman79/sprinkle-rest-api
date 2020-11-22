package com.kakaopay.sprinklerestapi.sprinkling.service;

import com.kakaopay.sprinklerestapi.sprinkling.domain.Sprinkling;
import com.kakaopay.sprinklerestapi.sprinkling.exception.*;
import com.kakaopay.sprinklerestapi.sprinkling.service.dto.CreateSprinklingRequestDto;
import org.springframework.stereotype.Component;

@Component
public class SprinklingValidator {
    private final long EXPIRE_RECEIVING_MINUTES = 10;

    private final long EXPIRE_READ_DAYS = 7;

    public void validateCreateRequest(CreateSprinklingRequestDto createRequest){
        if(createRequest.getSprinkledMoney() < createRequest.getPeopleCount()){
            throw new SprinkledMoneyLessThanPeopleCountException();
        }
    }

    public void validateReceiving(Sprinkling sprinkling, Long receiverId, String roomId, String token){
        if(!sprinkling.isEqualToToken(token)){
            throw new DifferentTokenException(token);
        }
        if(sprinkling.isExpiredReceiving(EXPIRE_RECEIVING_MINUTES)){
            throw new ExpiredReceivingException();
        }
        if(!sprinkling.isEqualToRoomId(roomId)){
            throw new DifferentRoomException(roomId);
        }
        if(sprinkling.isCreatorId(receiverId)){
            throw new CreatorCanNotReceiveException(receiverId);
        }
        if(sprinkling.isDuplicatedReceiverId(receiverId)){
            throw new DuplicatedReceiverException(receiverId);
        }
    }

    public void validateGetSprinkling(Sprinkling sprinkling, Long viewerId, String token){
        if(sprinkling.isExpiredRead(EXPIRE_READ_DAYS)){
            throw new ExpiredReadException();
        }
        if(!sprinkling.isCreatorId(viewerId)){
            throw new CreatorCanOnlyGetSprinklingException(viewerId);
        }
        if(!sprinkling.isEqualToToken(token)){
            throw new DifferentTokenException(token);
        }
    }
}
