package com.kakaopay.sprinklerestapi.response.error;

import com.kakaopay.sprinklerestapi.response.ApiResponseCode;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ApiError {

  private final LocalDateTime timestamp;
  private final ApiResponseCode code;
  private final String message;
  private String debugMessage;
  private List<ApiSubError> subErrors;

  public ApiError(ApiResponseCode code, String message, Throwable e) {
    this.timestamp = LocalDateTime.now();
    this.code = code;
    this.message = message;
    this.debugMessage = e.toString();
  }

  public ApiError(ApiResponseCode code, String message, List<ApiSubError> subErrors) {
    this.timestamp = LocalDateTime.now();
    this.code = code;
    this.message = message;
    this.subErrors = subErrors;
  }
}
