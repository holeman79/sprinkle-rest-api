package com.kakaopay.sprinklerestapi.sprinkling.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakaopay.sprinklerestapi.generic.money.domain.Money;
import com.kakaopay.sprinklerestapi.sprinkling.service.SprinklingCreateRequestDto;
import com.kakaopay.sprinklerestapi.sprinkling.service.SprinklingCreateResponseDto;
import com.kakaopay.sprinklerestapi.sprinkling.service.SprinklingService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    @Test
    @DisplayName("뿌리기 금액 또는 받기 인원 수가 0 이하일 경우 Bad Request")
    public void create_sprinkling_bad_request_amount_less_than_zero() throws Exception{
        //given
        Long creatorId = 1L;
        String roomId = "R1";

        Request request = new Request();
        request.amount = 0;
        request.peopleCount = 3;

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
        //given
        Long creatorId = 1L;
        String roomId = "R1";
        SprinklingCreateResponseDto sprinklingCreateResponseDto = SprinklingCreateResponseDto.builder()
                .id(1L)
                .roomId(roomId)
                .creatorId(creatorId)
                .peopleCount(4)
                .amount(Money.wons(1000))
                .token("k8A")
                .maxRandomMoney(Money.wons(250))
                .build();

        given(sprinklingService.create(any(SprinklingCreateRequestDto.class), eq(creatorId), eq(roomId)))
                .willReturn(sprinklingCreateResponseDto);

        Request request = new Request();
        request.amount = 1000l;
        request.peopleCount = 4;

        //when & then

        this.mockMvc.perform(post("/api/sprinklings")
                            .header(X_USER_ID, creatorId)
                            .header(X_ROOM_ID, roomId)
                            .content(this.objectMapper.writeValueAsString(request))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("data.id").value(1L))
                .andExpect(jsonPath("data.token").exists())
                ;
    }

    public static class Request {
        long amount;
        int peopleCount;

        public int getPeopleCount() {
            return peopleCount;
        }

        public long getAmount() {
            return amount;
        }
    }
}
