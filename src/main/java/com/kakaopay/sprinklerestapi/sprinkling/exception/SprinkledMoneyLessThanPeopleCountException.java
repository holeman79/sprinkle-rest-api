package com.kakaopay.sprinklerestapi.sprinkling.exception;

public class SprinkledMoneyLessThanPeopleCountException extends SprinkleException{
    public SprinkledMoneyLessThanPeopleCountException(){
        super("뿌리기 금액이 받을 인원수보다 커야합니다.");
    }
}
