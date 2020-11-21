package com.kakaopay.sprinklerestapi.sprinkling.service;

import com.kakaopay.sprinklerestapi.sprinkling.domain.Sprinkling;
import com.kakaopay.sprinklerestapi.sprinkling.exception.*;
import org.springframework.stereotype.Component;

@Component
public class SprinklingValidator {
    public void validateCreateRequest(SprinklingCreateRequestDto createRequest){
        if(createRequest.getAmount() < createRequest.getPeopleCount()){
            throw new AmountLessThanPeopleCountException("뿌리기 금액이 받을 인원수보다 커야합니다.");
        }
    }

    public void validateReceiving(Sprinkling sprinkling, Long receiverId, String roomId, String token){
        if(!sprinkling.isEqualToToken(token)){
            throw new SprinklingDiffrentTokenException(token);
        }
        if(sprinkling.isExpiredReceiving()){
            throw new SprinklingExpiredException();
        }
        if(!sprinkling.isEqualToRoomId(roomId)){
            throw new SprinklingDiffrentRoomException(roomId);
        }
        if(sprinkling.isCreatorId(receiverId)){
            throw new SprinklingCreatorCanNotReceiveException(receiverId);
        }
        if(sprinkling.isDuplicatedReceiverId(receiverId)){
            throw new SprinklingDuplicatedReceiverException(receiverId);
        }
    }
}
