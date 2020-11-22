package com.kakaopay.sprinklerestapi.sprinkling.exception;

public class CreatorCanOnlyGetSprinklingException extends SprinkleException {

  public CreatorCanOnlyGetSprinklingException(Long viewerId) {
    super("뿌린 사람이 아니면 뿌리기를 조회 할 수 없습니다. viewer ID : " + viewerId);
  }
}
