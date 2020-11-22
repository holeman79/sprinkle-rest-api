package com.kakaopay.sprinklerestapi.sprinkling.exception;

public class ExpiredReadException extends SprinkleException {

  public ExpiredReadException() {
    super("조회 시간이 만료 되었습니다.");
  }
}
