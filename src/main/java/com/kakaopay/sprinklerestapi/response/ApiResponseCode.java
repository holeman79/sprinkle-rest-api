package com.kakaopay.sprinklerestapi.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ApiResponseCode{
    OK("요청이 성공하였습니다."),
    BAD_PARAMETER("요청 파라미터가 잘못되었습니다."),
    MISSING_REQUEST_HEADER("필수 Header 정보가 누락되었습니다."),
    BAD_REQUEST_HEADER("요청 Header 정보가 누락되었습니다."),
    NOT_FOUND("리소스를 찾지 못했습니다."),
    UNAUTHORIZED("인증에 실패하였습니다."),
    SERVER_ERROR("서버 에러입니다.");

    private final String message;

    public String getId() {
        return name();
    }

    public String getText() {
        return message;
    }
}
