package com.kakaopay.sprinklerestapi.sprinkling.domain;

import com.kakaopay.sprinklerestapi.sprinkling.exception.AmountLessThanCountException;
import org.springframework.stereotype.Component;

import static com.kakaopay.sprinklerestapi.sprinkling.service.SprinklingDto.CreateRequest;

@Component
public class Validator {
    public void validate(CreateRequest createRequest){
        if(createRequest.getAmount() < createRequest.getPeopleCount()){
            throw new AmountLessThanCountException("뿌리기 금액이 받을 인원수보다 커야합니다.");
        }
    }
}
