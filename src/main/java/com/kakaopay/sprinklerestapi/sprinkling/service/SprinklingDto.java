package com.kakaopay.sprinklerestapi.sprinkling.service;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.Positive;

public class SprinklingDto {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class CreateRequest {
        @Positive(message = "뿌릴 금액을 양수로 입력해주세요.")
        private long amount;

        @Positive(message = "인원을 양수로 입력해주세요.")
        private int peopleCount;

    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class CreateResponse extends RepresentationModel<CreateResponse>{
        private Long id;
        private String token;
    }
}
