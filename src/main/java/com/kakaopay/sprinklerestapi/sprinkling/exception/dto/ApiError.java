package com.kakaopay.sprinklerestapi.sprinkling.exception.dto;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ApiError {

  private final LocalDateTime timestamp;
  private final HttpStatus status;
  private final String message;
  private String debugMessage;
  private List<ApiSubError> subErrors;

  public ApiError(HttpStatus status, String message, Throwable e) {
    this.timestamp = LocalDateTime.now();
    this.status = status;
    this.message = message;
    this.debugMessage = e.toString();
  }

  public ApiError(HttpStatus status, String message, List<ApiSubError> subErrors) {
    this.timestamp = LocalDateTime.now();
    this.status = status;
    this.message = message;
    this.subErrors = subErrors;
  }
}
