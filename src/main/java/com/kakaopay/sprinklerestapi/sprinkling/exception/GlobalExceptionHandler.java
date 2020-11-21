package com.kakaopay.sprinklerestapi.sprinkling.exception;

import com.kakaopay.sprinklerestapi.sprinkling.exception.dto.ApiError;
import com.kakaopay.sprinklerestapi.sprinkling.exception.dto.ApiSubError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  @ExceptionHandler(MissingRequestHeaderException.class)
  protected ResponseEntity<ApiError> handle(MissingRequestHeaderException e) {
    ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "필수 Header 정보가 누락되었습니다.", e);
    return new ResponseEntity<>(apiError, apiError.getStatus());
  }

  @ExceptionHandler(ConstraintViolationException.class)
  protected ResponseEntity<ApiError> handle(ConstraintViolationException e) {
    ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "요청 Header 정보가 잘못되었습니다.", e);
    return new ResponseEntity<>(apiError, apiError.getStatus());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  protected ResponseEntity<ApiError> handle(MethodArgumentNotValidException e) {
    List<ApiSubError> subErrors =
        e.getBindingResult().getFieldErrors().stream()
            .map(
                fieldError ->
                    ApiSubError.builder()
                        .object(fieldError.getObjectName())
                        .field(fieldError.getField())
                        .rejectedValue(fieldError.getRejectedValue())
                        .message(fieldError.getDefaultMessage())
                        .build())
            .collect(Collectors.toList());

    ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "요청 Body 정보가 잘못되었습니다.", subErrors);
    return new ResponseEntity<>(apiError, apiError.getStatus());
  }

  @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
  protected ResponseEntity<ApiError> handle(ObjectOptimisticLockingFailureException e) {
    ApiError apiError =
        new ApiError(
            HttpStatus.INTERNAL_SERVER_ERROR, "일시적으로 받기 요청을 처리하지 못했습니다. 잠시 후 다시 시도해주세요.", e);
    return new ResponseEntity<>(apiError, apiError.getStatus());
  }

  @ExceptionHandler(SprinklingNotFoundException.class)
  protected ResponseEntity<ApiError> handle(SprinklingNotFoundException e) {
    ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, e.getLocalizedMessage(), e);
    return new ResponseEntity<>(apiError, apiError.getStatus());
  }

  @ExceptionHandler(SprinkleException.class)
  protected ResponseEntity<ApiError> handle(SprinkleException e) {
    ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, e.getLocalizedMessage(), e);
    return new ResponseEntity<>(apiError, apiError.getStatus());
  }

  @ExceptionHandler(AmountLessThanPeopleCountException.class)
  protected ResponseEntity<ApiError> handle(AmountLessThanPeopleCountException e) {
    ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, e.getLocalizedMessage(), e);
    return new ResponseEntity<>(apiError, apiError.getStatus());
  }

  @ExceptionHandler(Exception.class)
  protected ResponseEntity<ApiError> handle(Exception e) {
    e.printStackTrace();
    ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "예기치 않은 예외가 발생하였습니다.", e);
    return new ResponseEntity<>(apiError, apiError.getStatus());
  }
}
