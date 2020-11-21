package com.kakaopay.sprinklerestapi.sprinkling.exception;

public class SprinklingFinishedReceivingException extends SprinkleException {

  public SprinklingFinishedReceivingException() {
    super("받기 인원이 마감 되었습니다. ");
  }
}
