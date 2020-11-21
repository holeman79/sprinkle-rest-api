package com.kakaopay.sprinklerestapi.sprinkling.exception;

public class SprinklingNotFoundException extends SprinkleException {

  public SprinklingNotFoundException(Long id) {
    super("해당 id 뿌리기 건이 존재하지 않습니다. id: " + id);
  }
}
