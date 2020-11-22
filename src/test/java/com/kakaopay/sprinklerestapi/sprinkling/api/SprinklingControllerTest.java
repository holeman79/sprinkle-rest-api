package com.kakaopay.sprinklerestapi.sprinkling.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakaopay.sprinklerestapi.generic.money.domain.Money;
import com.kakaopay.sprinklerestapi.sprinkling.service.dto.CreateSprinklingRequestDto;
import com.kakaopay.sprinklerestapi.sprinkling.service.dto.CreateSprinklingResponseDto;
import com.kakaopay.sprinklerestapi.sprinkling.service.SprinklingService;
import com.kakaopay.sprinklerestapi.sprinkling.service.dto.GetSprinklingResponseDto;
import com.kakaopay.sprinklerestapi.sprinkling.service.dto.UpdateSprinklingResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SprinklingController.class)
public class SprinklingControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected SprinklingService sprinklingService;

    private final String X_USER_ID = "X-USER-ID";

    private final String X_ROOM_ID = "X-ROOM-ID";

    private final String X_TOKEN = "X-TOKEN";

    @Test
    @DisplayName("뿌리기 금액 또는 받기 인원 수가 0 이하일 경우 Bad Request")
    public void create_sprinkling_request_less_than_zero() throws Exception{
        //given
        Long creatorId = 1L;
        String roomId = "R1";

        Request request = new Request();
        request.sprinkledMoney = 0;
        request.peopleCount = -3;

        this.mockMvc.perform(post("/api/sprinklings")
                .header(X_USER_ID, creatorId)
                .header(X_ROOM_ID, roomId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;
    }

    @Test
    @DisplayName("뿌리기 생성 성공 테스트")
    public void create_sprinkling_success() throws Exception{
        // given
        Long creatorId = 1L;
        String roomId = "R1";
        Long sprinklingId = 1L;
        String token = "k8A";
        CreateSprinklingResponseDto responseDto = CreateSprinklingResponseDto.builder()
                .id(sprinklingId)
                .roomId(roomId)
                .creatorId(creatorId)
                .peopleCount(4)
                .sprinkledMoney(Money.wons(1000))
                .token(token)
                .maxRandomMoney(Money.wons(250))
                .build();

        given(sprinklingService.create(any(CreateSprinklingRequestDto.class), eq(creatorId), eq(roomId)))
                .willReturn(responseDto);

        Request request = new Request();
        request.sprinkledMoney = 1000l;
        request.peopleCount = 4;

        // when
        String requestUrl = "/api/sprinklings";
        ResultActions perform = this.mockMvc.perform(post(requestUrl)
                .header(X_USER_ID, creatorId)
                .header(X_ROOM_ID, roomId)
                .content(this.objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
        );

        // then
        perform
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("data.id").value(sprinklingId))
                .andExpect(jsonPath("data.token").exists())
                .andExpect(jsonPath("code").value("OK"))
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.receiving").exists())
                .andExpect(jsonPath("_links.profile").exists())
                ;
    }

    @Test
    @DisplayName("받기 성공 테스트")
    public void receiving_success() throws Exception{
        // given
        Long sprinklingId = 1L;
        Long creatorId = 1L;
        Long receiverId = 101L;
        String roomId = "R1";
        String token = "aHZ";
        Long receivedMoney = 574L;

        UpdateSprinklingResponseDto responseDto = UpdateSprinklingResponseDto.builder()
                .id(sprinklingId)
                .creatorId(creatorId)
                .receiverId(receiverId)
                .roomId(roomId)
                .receivedMoney(receivedMoney)
                .isMaxRandomMoney(false)
                .build();

        given(sprinklingService.receive(sprinklingId, receiverId, roomId, token))
                .willReturn(responseDto);

        // when
        String requestUrl = "/api/sprinklings/{id}";
        ResultActions perform = this.mockMvc.perform(put(requestUrl, sprinklingId)
                .header(X_USER_ID, receiverId)
                .header(X_ROOM_ID, roomId)
                .header(X_TOKEN, token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
        );

        // then
        perform
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("data.id").value(sprinklingId))
                .andExpect(jsonPath("data.receiverId").value(receiverId))
                .andExpect(jsonPath("data.receivedMoney").value(receivedMoney))
                .andExpect(jsonPath("code").value("OK"))
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
        ;
    }

    @Test
    @DisplayName("조회 성공 테스트")
    public void find_by_id_success() throws Exception{
        // given
        Long sprinklingId = 1L;
        Long receivedMoney = 574L;
        String roomId = "R1";
        Long viewerId = 1L;
        String token = "aHZ";

        GetSprinklingResponseDto responseDto = GetSprinklingResponseDto.builder()
                .id(sprinklingId)
                .creatorId(viewerId)
                .receivedMoney(receivedMoney)
                .roomId(roomId)
                .build();

        given(sprinklingService.findById(sprinklingId, viewerId, token))
                .willReturn(responseDto);

        // when
        String requestUrl = "/api/sprinklings/{id}";
        ResultActions perform = this.mockMvc.perform(get(requestUrl, sprinklingId)
                .header(X_USER_ID, viewerId)
                .header(X_TOKEN, token)
        );

        // then
        perform
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("data.id").value(sprinklingId))
                .andExpect(jsonPath("data.creatorId").value(viewerId))
                .andExpect(jsonPath("data.receivedMoney").value(receivedMoney))
                .andExpect(jsonPath("data.roomId").value(roomId))
                .andExpect(jsonPath("code").value("OK"))
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.receiving").exists())
                .andExpect(jsonPath("_links.profile").exists())
        ;
    }



    public static class Request {
        long sprinkledMoney;
        int peopleCount;

        public int getPeopleCount() {
            return peopleCount;
        }

        public long getSprinkledMoney() {
            return sprinkledMoney;
        }
    }
}
