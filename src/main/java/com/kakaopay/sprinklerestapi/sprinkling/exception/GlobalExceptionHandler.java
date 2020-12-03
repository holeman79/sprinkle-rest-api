package com.kakaopay.sprinklerestapi.sprinkling.exception;

import com.kakaopay.sprinklerestapi.response.ApiResponseCode;
import com.kakaopay.sprinklerestapi.response.error.ApiError;
import com.kakaopay.sprinklerestapi.response.error.ApiSubError;
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
    ApiError apiError = new ApiError(ApiResponseCode.MISSING_REQUEST_HEADER, "필수 Header 정보가 누락되었습니다.", e);
    return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  protected ResponseEntity<ApiError> handle(ConstraintViolationException e) {
    ApiError apiError = new ApiError(ApiResponseCode.BAD_REQUEST_HEADER, "요청 Header 정보가 잘못되었습니다.", e);
    return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
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

    ApiError apiError = new ApiError(ApiResponseCode.BAD_PARAMETER, "요청 Body 정보가 잘못되었습니다.", subErrors);
    return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
  protected ResponseEntity<ApiError> handle(ObjectOptimisticLockingFailureException e) {
    ApiError apiError =
        new ApiError(
                ApiResponseCode.SERVER_ERROR, "일시적으로 받기 요청을 처리하지 못했습니다. 잠시 후 다시 시도해주세요.", e);
    return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(NotFoundException.class)
  protected ResponseEntity<ApiError> handle(NotFoundException e) {
    ApiError apiError = new ApiError(ApiResponseCode.NOT_FOUND, e.getLocalizedMessage(), e);
    return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(SprinkleException.class)
  protected ResponseEntity<ApiError> handle(SprinkleException e) {
    ApiError apiError = new ApiError(ApiResponseCode.BAD_PARAMETER, e.getLocalizedMessage(), e);
    return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(SprinkledMoneyLessThanPeopleCountException.class)
  protected ResponseEntity<ApiError> handle(SprinkledMoneyLessThanPeopleCountException e) {
    ApiError apiError = new ApiError(ApiResponseCode.BAD_PARAMETER, e.getLocalizedMessage(), e);
    return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(Exception.class)
  protected ResponseEntity<ApiError> handle(Exception e) {
    e.printStackTrace();
    ApiError apiError = new ApiError(ApiResponseCode.SERVER_ERROR, "예기치 않은 예외가 발생하였습니다.", e);
    return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
