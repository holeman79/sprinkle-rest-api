package com.kakaopay.sprinklerestapi.sprinkling.domain;

import com.kakaopay.sprinklerestapi.sprinkling.exception.AmountLessThanPeopleCountException;
import com.kakaopay.sprinklerestapi.sprinkling.exception.SprinklingCreatorCanNotReceiveException;
import com.kakaopay.sprinklerestapi.sprinkling.service.SprinklingCreateRequestDto;
import com.kakaopay.sprinklerestapi.sprinkling.service.SprinklingValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.kakaopay.sprinklerestapi.sprinkling.Fixtures.aSprinkling;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SprinklingValidatorTest {
    private SprinklingValidator sprinklingValidator;

    @BeforeEach
    public void setUp(){
        sprinklingValidator = new SprinklingValidator();
    }

    @Test
    @DisplayName("뿌릴 금액이 사람 수보다 적을 경우 실패")
    public void amount_less_than_people_count(){
        SprinklingCreateRequestDto createRequestDto = SprinklingCreateRequestDto.builder()
                .amount(5)
                .peopleCount(6)
                .build();
        assertThrows(AmountLessThanPeopleCountException.class, () ->
                sprinklingValidator.validateCreateRequest(createRequestDto));
    }

    @Test
    @DisplayName("뿌릴 금액이 사람 수보다 크거나 같을 경우 성공")
    public void amount_greater_than_or_equal_to_people_count(){
        SprinklingCreateRequestDto createRequestDto = SprinklingCreateRequestDto.builder()
                .amount(6)
                .peopleCount(6)
                .build();

        assertDoesNotThrow(() ->
                sprinklingValidator.validateCreateRequest(createRequestDto));
    }

    @Test
    @DisplayName("뿌리기 생성한 userId와 받기 수행하는 userId가 같을 때 CreatorCanNotReceive 에러발생")
    public void sprinklingCreatorCanNotReceiveException_발생() {
        Sprinkling sprinkling = aSprinkling().build();

        assertThrows(SprinklingCreatorCanNotReceiveException.class,
                ()-> sprinklingValidator.validateReceiving(sprinkling, 1L, "Room1", "a8a"));
    }
}
