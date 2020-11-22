package com.kakaopay.sprinklerestapi.sprinkling.exception;

public class ExpiredReceivingException extends SprinkleException {

  public ExpiredReceivingException() {
    super("받기 시간이 만료 되었습니다.");
  }
}
