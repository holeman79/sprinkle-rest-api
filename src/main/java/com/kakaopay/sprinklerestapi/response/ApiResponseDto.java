package com.kakaopay.sprinklerestapi.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@NoArgsConstructor
@Getter
public class ApiResponseDto<T> extends RepresentationModel<ApiResponseDto<T>> {

    private T data;
    private ApiResponseCode code;
    private String message;

    private ApiResponseDto(ApiResponseCode status) {
        this.bindStatus(status);
    }

    private ApiResponseDto(ApiResponseCode status, T data) {
        this.bindStatus(status);
        this.data = data;
    }

    private void bindStatus(ApiResponseCode status) {
        this.code = status;
        this.message = status.getMessage();
    }

    public static <T> ApiResponseDto<T> OK(T data) {
        return new ApiResponseDto<>(ApiResponseCode.OK, data);
    }
}
