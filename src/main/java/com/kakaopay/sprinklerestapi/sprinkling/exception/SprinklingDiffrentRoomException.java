package com.kakaopay.sprinklerestapi.sprinkling.exception;

public class SprinklingDiffrentRoomException extends SprinkleException {

  public SprinklingDiffrentRoomException(String roomId) {
    super("같은 방이 아닙니다. 요청 받은 방 ID: " + roomId);
  }
}
