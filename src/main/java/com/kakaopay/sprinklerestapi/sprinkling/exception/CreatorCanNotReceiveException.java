package com.kakaopay.sprinklerestapi.sprinkling.exception;

public class CreatorCanNotReceiveException extends SprinkleException {

  public CreatorCanNotReceiveException(Long receiverId) {
    super("뿌린 사람은 받기를 할 수 없습니다. receiverId : " + receiverId);
  }
}
