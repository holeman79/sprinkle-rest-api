package com.kakaopay.sprinklerestapi.sprinkling.exception;

public class ReceivingIsEmptyException extends SprinkleException {

  public ReceivingIsEmptyException() {
    super("Receiving 데이터가 없습니다.");
  }
}
