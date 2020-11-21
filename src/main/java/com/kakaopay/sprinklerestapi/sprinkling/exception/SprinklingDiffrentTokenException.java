package com.kakaopay.sprinklerestapi.sprinkling.exception;

public class SprinklingDiffrentTokenException extends SprinkleException {

  public SprinklingDiffrentTokenException(String token) {
    super("Token 값이 맞지 않습니다. 요청 받은 token: " + token);
  }
}
