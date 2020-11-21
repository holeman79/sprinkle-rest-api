package com.kakaopay.sprinklerestapi.sprinkling.exception;

public class SprinklingDuplicatedReceiverException extends SprinkleException {

  public SprinklingDuplicatedReceiverException(Long receiverId) {
    super("이미 받기를 완료한 ID입니다. receiver ID: " + receiverId);
  }
}
