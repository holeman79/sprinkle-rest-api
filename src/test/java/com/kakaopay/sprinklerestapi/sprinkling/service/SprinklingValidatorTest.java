package com.kakaopay.sprinklerestapi.sprinkling.service;

import com.kakaopay.sprinklerestapi.generic.money.domain.Money;
import com.kakaopay.sprinklerestapi.sprinkling.domain.Sprinkling;
import com.kakaopay.sprinklerestapi.sprinkling.exception.*;
import com.kakaopay.sprinklerestapi.sprinkling.service.dto.CreateSprinklingRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;

import static com.kakaopay.sprinklerestapi.sprinkling.Fixtures.aReceiving;
import static com.kakaopay.sprinklerestapi.sprinkling.Fixtures.aSprinkling;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SprinklingValidatorTest {
    private SprinklingValidator sprinklingValidator = new SprinklingValidator();

    @Test
    @DisplayName("뿌리기요청 파라미터 검증 : 뿌린 금액이 사람 수보다 적을 경우 실패")
    public void sprinkledMoney_less_than_people_count(){
        CreateSprinklingRequestDto createRequestDto = CreateSprinklingRequestDto.builder()
                .sprinkledMoney(5)
                .peopleCount(6)
                .build();
        assertThrows(SprinkledMoneyLessThanPeopleCountException.class, () ->
                sprinklingValidator.validateCreateRequest(createRequestDto));
    }

    @Test
    @DisplayName("뿌리기요청 파라미터 검증 : 뿌린 금액이 사람 수보다 크거나 같을 경우 성공")
    public void sprinkledMoney_greater_than_or_equal_to_people_count(){
        CreateSprinklingRequestDto createRequestDto = CreateSprinklingRequestDto.builder()
                .sprinkledMoney(6)
                .peopleCount(6)
                .build();

        assertDoesNotThrow(() ->
                sprinklingValidator.validateCreateRequest(createRequestDto));
    }

    @Test
    @DisplayName("받기 검증 : 요청받은 token이 뿌리기에서 발급한 token과 일치하지 않을 때")
    public void different_token_exception_발생(){
        String sprinklingToken = "a8z";
        String requestToken = "bFj";

        String roomId = "R1";
        Long creatorId = 1L;
        Long receiverId = 101L;

        Sprinkling sprinkling = aSprinkling().creatorId(creatorId).roomId(roomId).token(sprinklingToken).build();

        assertThrows( DifferentTokenException.class, () ->
                sprinklingValidator.validateReceiving(sprinkling, receiverId, roomId, requestToken));
    }

    @Test
    @DisplayName("받기 검증 : 받기 유효시간 10분이 초과했을 때")
    public void expired_receiving_exception_발생(){
        String token = "a8z";
        String roomId = "R1";
        Long creatorId = 1L;
        Long receiverId = 101L;

        LocalDateTime sprinkledTime = LocalDateTime.now().minusMinutes(10);

        Sprinkling sprinkling = aSprinkling().creatorId(creatorId).roomId(roomId).token(token)
                                .sprinkledTime(sprinkledTime).build();

        assertThrows( ExpiredReceivingException.class, () ->
                sprinklingValidator.validateReceiving(sprinkling, receiverId, roomId, token));
    }

    @Test
    @DisplayName("받기 검증 : 받기요청 받은 room id가 뿌리기 room id와 다를 때")
    public void different_room_exception_발생(){
        String token = "a8z";
        Long creatorId = 1L;
        Long receiverId = 101L;

        String sprinklingRoomId = "R1";
        String requestRoomId = "R2";

        Sprinkling sprinkling = aSprinkling().creatorId(creatorId).roomId(sprinklingRoomId).token(token).build();

        assertThrows( DifferentRoomException.class, () ->
                sprinklingValidator.validateReceiving(sprinkling, receiverId, requestRoomId, token));
    }



    @Test
    @DisplayName("받기 검증 : 뿌리기 생성한 userId와 받기 수행하는 userId가 같을 때 CreatorCanNotReceive 에러발생")
    public void creator_can_not_receive_exception_발생() {
        Long creatorId = 1L;
        Long receiverId = creatorId;

        String roomId = "R1";
        String token = "a8z";

        Sprinkling sprinkling = aSprinkling().creatorId(creatorId).roomId(roomId).token(token).build();

        assertThrows(CreatorCanNotReceiveException.class,
                ()-> sprinklingValidator.validateReceiving(sprinkling, receiverId, roomId, token));
    }

    @Test
    @DisplayName("받기 검증 : 이미 받기를 완료한 사용자가 다시 받기 요청을 했을 때")
    public void duplicated_receiver_exception_발생() {
        Long creatorId = 1L;
        Long receiverId = 101L;

        String roomId = "R1";
        String token = "a8z";

        Sprinkling sprinkling = aSprinkling().creatorId(creatorId).roomId(roomId).token(token).receivings(Arrays.asList(
                aReceiving()
                        .receiverId(101L)
                        .receivedMoney(Money.wons(170))
                        .build(),
                aReceiving()
                        .receiverId(102L)
                        .receivedMoney(Money.wons(480))
                        .build()
        )).build();

        assertThrows(DuplicatedReceiverException.class,
                ()-> sprinklingValidator.validateReceiving(sprinkling, receiverId, roomId, token));
    }

    @Test
    @DisplayName("받기 성공 : 모든 조건이 만족했을 때 성공 테스트")
    public void receiving_success() {
        Long creatorId = 1L;
        Long receiverId = 102L;

        String roomId = "R1";
        String token = "a8z";

        Sprinkling sprinkling = aSprinkling().creatorId(creatorId).peopleCount(2).roomId(roomId).token(token).receivings(Arrays.asList(
                aReceiving()
                        .id(1L)
                        .receiverId(101L)
                        .receivedMoney(Money.wons(170))
                        .build(),
                aReceiving()
                        .id(2L)
                        .receivedMoney(Money.wons(480))
                        .build()
        )).build();

        assertDoesNotThrow(()-> sprinklingValidator.validateReceiving(sprinkling, receiverId, roomId, token));
    }

    @Test
    @DisplayName("조회 검증 : 조회 유효시간 7일이 초과했을 때")
    public void expired_read_exception_발생(){
        String token = "a8z";
        String roomId = "R1";
        Long creatorId = 1L;
        Long viewerId = creatorId;

        LocalDateTime sprinkledTime = LocalDateTime.now().minusDays(7);

        Sprinkling sprinkling = aSprinkling().creatorId(creatorId).roomId(roomId).token(token)
                .sprinkledTime(sprinkledTime).build();

        assertThrows( ExpiredReadException.class, () ->
                sprinklingValidator.validateGetSprinkling(sprinkling, viewerId, token));
    }

    @Test
    @DisplayName("조회 검증 : 조회자 id와 뿌리기 생성자 id가 다를 때")
    public void creator_can_only_get_sprinkling_exception_발생(){
        String token = "a8z";
        Long creatorId = 1L;
        Long viewerId = 2L;

        Sprinkling sprinkling = aSprinkling().creatorId(creatorId).token(token).build();

        assertThrows( CreatorCanOnlyGetSprinklingException.class, () ->
                sprinklingValidator.validateGetSprinkling(sprinkling, viewerId, token));
    }

    @Test
    @DisplayName("조회 검증 : 조회 요청받은 token과 뿌리기 token이 다을 때")
    public void get_different_token_exception_발생(){
        String token = "a8z";
        String requestToken = "16Z";
        Long creatorId = 1L;
        Long viewerId = creatorId;

        Sprinkling sprinkling = aSprinkling().creatorId(creatorId).token(token).build();

        assertThrows( DifferentTokenException.class, () ->
                sprinklingValidator.validateGetSprinkling(sprinkling, viewerId, requestToken));
    }

    @Test
    @DisplayName("조회 성공 : 모든 조건이 만족했을 때 성공 테스트")
    public void get_success() {
        String token = "a8z";
        Long creatorId = 1L;
        Long viewerId = creatorId;

        Sprinkling sprinkling = aSprinkling().creatorId(creatorId).token(token).build();

        assertDoesNotThrow(()-> sprinklingValidator.validateGetSprinkling(sprinkling, viewerId, token));
    }
}
