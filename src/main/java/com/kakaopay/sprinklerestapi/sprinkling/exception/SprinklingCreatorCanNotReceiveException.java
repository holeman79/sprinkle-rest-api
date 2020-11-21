package com.kakaopay.sprinklerestapi.sprinkling.exception;

public class SprinklingCreatorCanNotReceiveException extends SprinkleException {

  public SprinklingCreatorCanNotReceiveException(Long receiverId) {
    super("뿌린 사람은 받기를 할 수 없습니다. receiverId : " + receiverId);
  }
}
