package com.kakaopay.sprinklerestapi.sprinkling.exception;

public class FinishedReceivingException extends SprinkleException {

  public FinishedReceivingException() {
    super("받기 인원이 마감 되었습니다. ");
  }
}
