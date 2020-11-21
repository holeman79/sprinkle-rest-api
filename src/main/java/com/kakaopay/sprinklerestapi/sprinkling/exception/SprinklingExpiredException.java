package com.kakaopay.sprinklerestapi.sprinkling.exception;

public class SprinklingExpiredException extends SprinkleException {

  public SprinklingExpiredException() {
    super("받기 시간이 만료 되었습니다. ");
  }
}
