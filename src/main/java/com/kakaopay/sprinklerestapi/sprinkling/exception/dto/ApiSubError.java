package com.kakaopay.sprinklerestapi.sprinkling.exception.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ApiSubError {

  private final String object;
  private final String field;
  private final Object rejectedValue;
  private final String message;
}
