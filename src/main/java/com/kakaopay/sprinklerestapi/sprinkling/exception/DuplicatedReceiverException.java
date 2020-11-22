package com.kakaopay.sprinklerestapi.sprinkling.exception;

public class DuplicatedReceiverException extends SprinkleException {

  public DuplicatedReceiverException(Long receiverId) {
    super("이미 받기를 완료한 ID입니다. receiver ID: " + receiverId);
  }
}
